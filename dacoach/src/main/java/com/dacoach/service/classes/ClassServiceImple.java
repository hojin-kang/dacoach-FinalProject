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
	
	private static final int LIST_SIZE = 4;  // 한 페이지당 카드 수
	
	@Override
	@Transactional
	public int classRegister(ClassDTO classDTO, String hashtags) throws Exception {
		try {
			// 1. 멤버십 기반 클래스 등록 가능 여부 확인
			if (!canRegisterClass(classDTO.getProvider_idx())) {
				throw new IllegalArgumentException("MEMBERSHIP_LIMIT");
			}

			// 2. 유효성 검증
			validateClassDTO(classDTO);

			// 3. 클래스 정보 DB 저장
			int result = classMapper.insertClass(classDTO);

			if (result <= 0) {
				throw new RuntimeException("클래스 등록에 실패했습니다.");
			}

			// 4. 해시태그 처리
			if (hashtags != null && !hashtags.trim().isEmpty()) {
				// 콤마로 구분된 해시태그 분리
				String[] tagArray = hashtags.split(",");

				for (String tag : tagArray) {
					tag = tag.trim();
					if (tag.isEmpty())
						continue;

					// 4-1. 해시태그가 이미 존재하는지 확인
					Integer hashtagIdx = classMapper.findHashtagByName(tag);

					// 4-2. 존재하지 않으면 새로 INSERT
					if (hashtagIdx == null) {
						classMapper.insertHashtag(tag);
						hashtagIdx = classMapper.findHashtagByName(tag);
					}

					// 4-3. CLASS_HASHTAG 매핑 테이블에 저장
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
		if (classDTO.getRegion_detail() == null) {
			throw new IllegalArgumentException("상세 주소를 입력해주세요.");
		}
		if (classDTO.getPrice() == null || classDTO.getPrice() < 0) {
			throw new IllegalArgumentException("올바른 가격을 입력해주세요.");
		}
		if (classDTO.getPhoto() == null) {
			throw new IllegalArgumentException("사진을 입력해주세요.");
		}
		if (classDTO.getVideo() == null) {
			throw new IllegalArgumentException("영상을 입력해주세요.");
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

		// 날짜 검증 (오늘 날짜도 허용)
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
		cal.set(java.util.Calendar.MINUTE, 0);
		cal.set(java.util.Calendar.SECOND, 0);
		cal.set(java.util.Calendar.MILLISECOND, 0);
		Date today = cal.getTime();

		// 어제 날짜 계산
		cal.add(java.util.Calendar.DATE, -1);
		Date yesterday = cal.getTime();

		// 시작일이 어제보다 이전인지 확인 (오늘은 허용)
		if (classDTO.getStart_date().before(yesterday)) {
			throw new IllegalArgumentException("시작일은 오늘 이후여야 합니다.");
		}

		// 종료일이 시작일보다 이전인지 확인 (같은 날짜는 허용)
		if (classDTO.getEnd_date().before(classDTO.getStart_date())) {
			throw new IllegalArgumentException("종료일은 시작일과 같거나 이후여야 합니다.");
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
	public List<ClassDTO> getClassesByProvider(Integer providerIdx, String sort, int cp) throws Exception {
	    if (providerIdx == null) throw new IllegalArgumentException("provider_idx가 필요합니다.");
	    if (sort == null || sort.isEmpty()) sort = "all";

	    int startRow = (cp - 1) * LIST_SIZE + 1;
	    int endRow   = cp * LIST_SIZE;

	    Map<String, Object> params = new HashMap<>();
	    params.put("provider_idx", providerIdx);
	    params.put("sort", sort);
	    params.put("startRow", startRow);
	    params.put("endRow", endRow);

	    return classMapper.selectClassesByProvider(params);
	}

	@Override
	public int countClassesByProvider(Integer providerIdx, String sort) throws Exception {
	    if (providerIdx == null) throw new IllegalArgumentException("provider_idx가 필요합니다.");
	    if (sort == null || sort.isEmpty()) sort = "all";

	    Map<String, Object> params = new HashMap<>();
	    params.put("provider_idx", providerIdx);
	    params.put("sort", sort);

	    return classMapper.countClassesByProvider(params);
	}
	
	@Override
	public List<ClassDTO> getAllClassesByProvider(Integer providerIdx) throws Exception {
	    Map<String, Object> params = new HashMap<>();
	    params.put("provider_idx", providerIdx);
	    params.put("sort", "all");
	    params.put("startRow", 1);
	    params.put("endRow", 9999);
	    return classMapper.selectClassesByProvider(params);
	}

	@Override
	public ClassDTO getClassDetail(int classIdx) throws Exception {
		return classMapper.getClassDetail(classIdx);
	}

	@Override
	public Map<String, Object> getProviderInfo(int provider_idx) throws Exception {
		return classMapper.selectProviderInfo(provider_idx);
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

		// 확정된 신청: CONFIRMED 상태이면서 수강 예정일이 오늘이거나 미래인 건
		Date today = new Date();
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(today);
		cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
		cal.set(java.util.Calendar.MINUTE, 0);
		cal.set(java.util.Calendar.SECOND, 0);
		cal.set(java.util.Calendar.MILLISECOND, 0);
		Date todayStart = cal.getTime();

		long activeStudents = enrollments.stream().filter(e -> "CONFIRMED".equals(e.get("STATUS"))).filter(e -> {
			Date completedAt = (Date) e.get("COMPLETED_AT");
			if (completedAt == null)
				return false;

			java.util.Calendar completedCal = java.util.Calendar.getInstance();
			completedCal.setTime(completedAt);
			completedCal.set(java.util.Calendar.HOUR_OF_DAY, 0);
			completedCal.set(java.util.Calendar.MINUTE, 0);
			completedCal.set(java.util.Calendar.SECOND, 0);
			completedCal.set(java.util.Calendar.MILLISECOND, 0);
			Date completedDateOnly = completedCal.getTime();

			return !completedDateOnly.before(todayStart); // 오늘이거나 미래
		}).count();

		int totalRevenue = classDTO.getPrice() * totalEnrollments; // 전체 신청 건수 기준

		result.put("totalEnrollments", totalEnrollments);
		result.put("activeStudents", activeStudents);
		result.put("totalRevenue", totalRevenue);

		// 수강생 정보
		result.put("students", enrollments);

		// 시간별 수강 신청 통계 (신청일 기준)
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

		// 수강 예정일별 통계 추가 (COMPLETED_AT 기준)
		Map<String, Long> completedByDateMap = enrollments.stream().filter(e -> e.get("COMPLETED_AT") != null)
				.collect(Collectors.groupingBy(e -> sdf.format((Date) e.get("COMPLETED_AT")), Collectors.counting()));

		List<Map<String, Object>> completedByDate = new ArrayList<>();
		for (Map.Entry<String, Long> entry : completedByDateMap.entrySet()) {
			Map<String, Object> dateData = new HashMap<>();
			dateData.put("date", entry.getKey());
			dateData.put("count", entry.getValue());
			completedByDate.add(dateData);
		}

		// 날짜순 정렬
		completedByDate.sort((a, b) -> ((String) a.get("date")).compareTo((String) b.get("date")));

		result.put("completedByDate", completedByDate);

		// 수익 통계 (누적)
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

	@Override
	@Transactional
	public int classUpdate(ClassDTO classDTO, String hashtags) throws Exception {
		try {
			// 1. 기존 클래스 정보 조회 (시작일 변경 여부 확인용)
			ClassDTO existingClass = classMapper.getClassDetail(classDTO.getClass_idx());
			if (existingClass == null) {
				throw new IllegalArgumentException("존재하지 않는 클래스입니다.");
			}

			// 2. 유효성 검증 (수정용 - 기존 정보 전달)
			validateClassUpdateDTO(classDTO, existingClass);

			// 3. 클래스 기본 정보 수정
			int result = classMapper.updateClass(classDTO);

			if (result <= 0) {
				throw new RuntimeException("클래스 수정에 실패했습니다.");
			}

			// 4. 해시태그 처리
			// 4-1. 기존 해시태그 매핑 모두 삭제
			classMapper.deleteClassHashtags(classDTO.getClass_idx());

			// 4-2. 새로운 해시태그 등록
			if (hashtags != null && !hashtags.trim().isEmpty()) {
				String[] tagArray = hashtags.split(",");

				for (String tag : tagArray) {
					tag = tag.trim();
					if (tag.isEmpty())
						continue;

					// 해시태그가 이미 존재하는지 확인
					Integer hashtagIdx = classMapper.findHashtagByName(tag);

					// 존재하지 않으면 새로 INSERT
					if (hashtagIdx == null) {
						classMapper.insertHashtag(tag);
						hashtagIdx = classMapper.findHashtagByName(tag);
					}

					// CLASS_HASHTAG 매핑 테이블에 저장
					if (hashtagIdx != null) {
						classMapper.insertClassHashtag(classDTO.getClass_idx(), hashtagIdx);
					}
				}
			}

			return result;

		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("클래스 수정 중 오류가 발생했습니다: " + e.getMessage(), e);
		}
	}

	/**
	 * 클래스 수정 시 유효성 검증 (제목, 분야, 가격은 수정 불가이므로 검증에서 제외)
	 */
	private void validateClassUpdateDTO(ClassDTO classDTO, ClassDTO existingClass) throws Exception {
		// 필수 입력 확인
		if (classDTO.getClass_idx() == null) {
			throw new IllegalArgumentException("클래스 IDX가 필요합니다.");
		}
		if (classDTO.getIntro() == null || classDTO.getIntro().trim().isEmpty()) {
			throw new IllegalArgumentException("내용을 입력해주세요.");
		}
		if (classDTO.getMinor_region_idx() == null) {
			throw new IllegalArgumentException("클래스 장소를 선택해주세요.");
		}
		if (classDTO.getRegion_detail() == null) {
			throw new IllegalArgumentException("상세 주소를 입력해주세요.");
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
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
		cal.set(java.util.Calendar.MINUTE, 0);
		cal.set(java.util.Calendar.SECOND, 0);
		cal.set(java.util.Calendar.MILLISECOND, 0);
		Date today = cal.getTime();

		// 어제 날짜 계산
		cal.add(java.util.Calendar.DATE, -1);
		Date yesterday = cal.getTime();

		// 시작일이 변경되었는지 확인
		boolean startDateChanged = !isSameDate(classDTO.getStart_date(), existingClass.getStart_date());

		// 시작일이 변경된 경우에만 오늘 이후인지 검증
		if (startDateChanged && classDTO.getStart_date().before(yesterday)) {
			throw new IllegalArgumentException("시작일은 오늘 이후여야 합니다.");
		}

		// 종료일이 시작일보다 이전인지 확인 (같은 날짜는 허용)
		if (classDTO.getEnd_date().before(classDTO.getStart_date())) {
			throw new IllegalArgumentException("종료일은 시작일과 같거나 이후여야 합니다.");
		}

		// 내용 길이 검증
		if (classDTO.getIntro().length() > 3000) {
			throw new IllegalArgumentException("내용은 3000자 이내로 입력해주세요.");
		}
	}

	/**
	 * 두 날짜가 같은 날인지 비교 (시간 무시)
	 */
	private boolean isSameDate(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}

		java.util.Calendar cal1 = java.util.Calendar.getInstance();
		cal1.setTime(date1);

		java.util.Calendar cal2 = java.util.Calendar.getInstance();
		cal2.setTime(date2);

		return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR)
				&& cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH)
				&& cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
	}

	@Override
	public List<String> getHashtagsByClass(int class_idx) throws Exception {
		return classMapper.selectHashtagsByClass(class_idx);
	}

	@Override
	public Map<String, Object> getClassFieldInfo(int class_idx) throws Exception {
		return classMapper.selectClassFieldInfo(class_idx);
	}

	@Override
	public Map<String, Object> getClassRegionInfo(int class_idx) throws Exception {
		return classMapper.selectClassRegionInfo(class_idx);
	}

	@Override
	public Map<String, Object> getRegionInfoByMinorIdx(Integer minorRegionIdx) throws Exception {
		if (minorRegionIdx == null) {
			throw new IllegalArgumentException("지역 IDX가 필요합니다.");
		}
		return classMapper.getRegionInfoByMinorIdx(minorRegionIdx);
	}

	// ⭐ 리뷰 관련 메서드 구현
	@Override
	public List<Map<String, Object>> getReviewsByClass(int class_idx) throws Exception {
		return classMapper.selectReviewsByClass(class_idx);
	}

	@Override
	public Double getAvgRatingByClass(int class_idx) throws Exception {
		Double avgRating = classMapper.selectAvgRatingByClass(class_idx);
		return avgRating != null ? avgRating : 0.0;
	}

	@Override
	public Integer getReviewCountByClass(int class_idx) throws Exception {
		Integer count = classMapper.selectReviewCountByClass(class_idx);
		return count != null ? count : 0;
	}

	@Override
	public List<Map<String, Object>> getAllReviewsByProvider(int provider_idx) throws Exception {
		// 1. 제공자의 모든 클래스 IDX 조회
		List<Integer> classIdxList = classMapper.selectClassIdxByProvider(provider_idx);

		// 2. 모든 클래스의 리뷰를 모아서 반환
		List<Map<String, Object>> allReviews = new ArrayList<>();

		for (Integer classIdx : classIdxList) {
			List<Map<String, Object>> reviews = classMapper.selectReviewsByClass(classIdx);

			// 각 리뷰에 클래스 정보 추가
			for (Map<String, Object> review : reviews) {
				ClassDTO classDTO = classMapper.getClassDetail(classIdx);
				review.put("CLASS_TITLE", classDTO.getTitle());
				allReviews.add(review);
			}
		}

		// 최신순 정렬 (CREATED_AT 기준)
		allReviews.sort((a, b) -> {
			Date dateA = (Date) a.get("CREATED_AT");
			Date dateB = (Date) b.get("CREATED_AT");
			return dateB.compareTo(dateA); // 내림차순
		});

		return allReviews;
	}

	// ⭐ 멤버십 기반 클래스 등록 가능 여부 확인
	@Override
	public boolean canRegisterClass(int user_idx) throws Exception {
		// 1. 유저의 멤버십 상세 정보 조회
		Map<String, Object> membershipDetail = classMapper.selectMembershipDetail(user_idx);

		if (membershipDetail == null) {
			// 멤버십 정보가 없는 경우 등록 불가
			return false;
		}

		// 2. 멤버십의 최대 클래스 개수 조회 (숫자를 문자열로 변환 후 Integer로)
		Object classMaxCntObj = membershipDetail.get("CLASS_MAX_CNT");
		if (classMaxCntObj == null) {
			return false;
		}
		int classMaxCnt = Integer.parseInt(classMaxCntObj.toString());

		if (classMaxCnt <= 0) {
			return false;
		}

		// 3. 현재 활성 클래스 개수 조회 (종료되지 않은 클래스)
		Object activeClassCountObj = classMapper.countActiveClassesByProvider(user_idx);
		int activeClassCount = 0;

		if (activeClassCountObj != null) {
			activeClassCount = Integer.parseInt(activeClassCountObj.toString());
		}

		// 4. 활성 클래스 개수가 최대 개수보다 적으면 등록 가능
		return activeClassCount < classMaxCnt;
	}

	@Override
	public Map<String, Object> getMembershipDetail(int user_idx) throws Exception {
		return classMapper.selectMembershipDetail(user_idx);
	}

}