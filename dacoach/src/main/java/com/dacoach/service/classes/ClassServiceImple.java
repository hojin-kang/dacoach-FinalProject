package com.dacoach.service.classes;

import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.mapper.classes.ClassMapper;
import com.dacoach.model.classes.ClassDTO;

@Service
public class ClassServiceImple implements ClassService {

	@Autowired
	private ClassMapper classMapper;

	@Override
	@Transactional
	public int classRegister(ClassDTO classDTO, String hashtags) throws Exception {
		try {
			// 1. 유효성 검증
			validateClassDTO(classDTO);

			// 2. 클래스 정보 DB 저장
			int result = classMapper.insertClass(classDTO);

			if (result <= 0) {
				throw new RuntimeException("클래스 등록에 실패했습니다.");
			}

			// ⭐ 3. 해시태그 처리
			if (hashtags != null && !hashtags.trim().isEmpty()) {
				// 콤마로 구분된 해시태그 분리
				String[] tagArray = hashtags.split(",");

				for (String tag : tagArray) {
					tag = tag.trim();
					if (tag.isEmpty())
						continue;

					// 3-1. 해시태그가 이미 존재하는지 확인
					Integer hashtagIdx = classMapper.findHashtagByName(tag);

					// 3-2. 존재하지 않으면 새로 INSERT
					if (hashtagIdx == null) {
						classMapper.insertHashtag(tag);
						hashtagIdx = classMapper.findHashtagByName(tag);
					}

					// 3-3. CLASS_HASHTAG 매핑 테이블에 저장
					if (hashtagIdx != null) {
						classMapper.insertClassHashtag(classDTO.getClass_idx(), hashtagIdx);
					}
				}
			}

			return result;
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("클래스 등록 중 오류가 발생했습니다: " + e.getMessage(), e);
		}
	}

	private void validateClassDTO(ClassDTO classDTO) throws Exception {
		// 필수 입력 확인
		if (classDTO.getTitle() == null || classDTO.getTitle().trim().isEmpty()) {
			throw new IllegalArgumentException("제목을 입력해주세요.");
		}
		if (classDTO.getIntro() == null || classDTO.getIntro().trim().isEmpty()) {
			throw new IllegalArgumentException("내용을 입력해주세요.");
		}
		if (classDTO.getMinor_field_idx() == null) {
			throw new IllegalArgumentException("클래스 분야를 선택해주세요.");
		}
		if (classDTO.getMinor_region_idx() == null) {
			throw new IllegalArgumentException("클래스 장소를 선택해주세요.");
		}
		if (classDTO.getPrice() == null || classDTO.getPrice() < 0) {
			throw new IllegalArgumentException("올바른 가격을 입력해주세요.");
		}
		if (classDTO.getStart_date() == null) {
			throw new IllegalArgumentException("시작일을 입력해주세요.");
		}
		if (classDTO.getEnd_date() == null) {
			throw new IllegalArgumentException("종료일을 입력해주세요.");
		}
		if (classDTO.getMax_user_cnt() == null || classDTO.getMax_user_cnt() <= 0) {
			throw new IllegalArgumentException("수강 인원을 올바르게 입력해주세요.");
		}

		// 날짜 검증
		Date now = new Date();
		if (classDTO.getStart_date().before(now)) {
			throw new IllegalArgumentException("시작일은 현재 시간 이후여야 합니다.");
		}
		if (classDTO.getEnd_date().before(classDTO.getStart_date())
				|| classDTO.getEnd_date().equals(classDTO.getStart_date())) {
			throw new IllegalArgumentException("종료일은 시작일보다 이후여야 합니다.");
		}

		// 제목/내용 길이 검증
		if (classDTO.getTitle().length() > 200) {
			throw new IllegalArgumentException("제목은 200자 이내로 입력해주세요.");
		}
		if (classDTO.getIntro().length() > 3000) {
			throw new IllegalArgumentException("내용은 3000자 이내로 입력해주세요.");
		}
	}

	@Override
	public List<Map<String, Object>> getMajorFields() throws Exception {
		return classMapper.selectMajorFields();
	}

	@Override
	public List<Map<String, Object>> getMinorFields(Integer majorFieldIdx) throws Exception {
		if (majorFieldIdx == null) {
			throw new IllegalArgumentException("대분류를 선택해주세요.");
		}
		return classMapper.selectMinorFields(majorFieldIdx);
	}

	@Override
	public List<Map<String, Object>> getMajorRegions() throws Exception {
		return classMapper.selectMajorRegions();
	}

	@Override
	public List<Map<String, Object>> getMinorRegions(Integer majorRegionIdx) throws Exception {
		if (majorRegionIdx == null) {
			throw new IllegalArgumentException("대지역을 선택해주세요.");
		}
		return classMapper.selectMinorRegions(majorRegionIdx);
	}

	@Override
	public List<ClassDTO> getClassesByProvider(Integer providerIdx) throws Exception {
		if (providerIdx == null) {
			throw new IllegalArgumentException("provider_idx가 필요합니다.");
		}
		return classMapper.selectClassesByProvider(providerIdx);
	}

	@Override
	public ClassDTO getClassDetail(int classIdx) throws Exception {
		return classMapper.getClassDetail(classIdx);
	}

	@Override
	public Map<String, Object> getClassStats(int classIdx, int providerIdx) throws Exception {
		// 클래스 소유자 확인
		ClassDTO classDTO = classMapper.getClassDetail(classIdx);
		if (classDTO == null) {
			throw new IllegalArgumentException("존재하지 않는 클래스입니다.");
		}
		if (classDTO.getProvider_idx() != providerIdx) {
			throw new IllegalArgumentException("권한이 없습니다.");
		}

		// 통계 데이터 조회
		List<Map<String, Object>> enrollments = classMapper.selectEnrollmentsByClass(classIdx);

		Map<String, Object> result = new HashMap<>();

		// 기본 통계
		int totalEnrollments = enrollments.size();
		long activeStudents = enrollments.stream().filter(e -> "ENROLLED".equals(e.get("STATUS"))).count();
		int totalRevenue = classDTO.getPrice() * (int) activeStudents;

		result.put("totalEnrollments", totalEnrollments);
		result.put("activeStudents", activeStudents);
		result.put("totalRevenue", totalRevenue);

		// 수강생 정보
		result.put("students", enrollments);

		// 시간별 수강 신청 통계 (날짜별 집계)
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
		Map<String, Long> enrollmentByDateMap = enrollments.stream().filter(e -> e.get("ENROLLED_AT") != null)
				.collect(Collectors.groupingBy(e -> sdf.format((Date) e.get("ENROLLED_AT")), Collectors.counting()));

		List<Map<String, Object>> enrollmentByDate = new ArrayList<>();
		for (Map.Entry<String, Long> entry : enrollmentByDateMap.entrySet()) {
			Map<String, Object> dateData = new HashMap<>();
			dateData.put("date", entry.getKey());
			dateData.put("count", entry.getValue());
			enrollmentByDate.add(dateData);
		}

		// 날짜순 정렬
		enrollmentByDate.sort((a, b) -> ((String) a.get("date")).compareTo((String) b.get("date")));

		result.put("enrollmentByDate", enrollmentByDate);

		// 수입 통계 (누적)
		List<Map<String, Object>> revenueByDate = new ArrayList<>();
		int cumulativeRevenue = 0;
		for (Map<String, Object> dateData : enrollmentByDate) {
			cumulativeRevenue += ((Long) dateData.get("count")).intValue() * classDTO.getPrice();
			Map<String, Object> revenueData = new HashMap<>();
			revenueData.put("date", dateData.get("date"));
			revenueData.put("revenue", cumulativeRevenue);
			revenueByDate.add(revenueData);
		}

		result.put("revenueByDate", revenueByDate);

		return result;
	}

}