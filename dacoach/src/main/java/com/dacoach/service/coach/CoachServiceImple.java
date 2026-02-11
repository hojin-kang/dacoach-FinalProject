package com.dacoach.service.coach;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dacoach.mapper.coach.CoachMapper;
import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.users.UsersDTO;
import com.dacoach.service.file.FileUpload;

@Service
@Transactional
public class CoachServiceImple implements CoachService {
	@Autowired
	private CoachMapper coachMapper;

	@Override
	public boolean idCheck(String username) throws Exception {
		boolean result=coachMapper.idCheck(username)>0?true:false;
		return result;
	}

	@Override
	public Integer coachProfile(UsersDTO udto) throws Exception {
		udto.setPassword(com.dacoach.javasecure.JavaDataSecureModule.getSHA256(udto.getPassword()));
		Integer result=coachMapper.coachProfile(udto);
		return result;
	}

	@Override
	public boolean checkNick(String nickname) throws Exception {
		boolean result=coachMapper.checkNick(nickname)>0?true:false;
		return result;
	}

	@Override
	public List<Map<String, Object>> getMajorFields() throws Exception {
		return coachMapper.getMajorFields();
	}

	@Override
	public List<Map<String, Object>> getMinorFields(int majorIdx) throws Exception {
		return coachMapper.getMinorFields(majorIdx);
	}

	@Override
	public List<Map<String, Object>> getMajorRegions() throws Exception {
		return coachMapper.getMajorRegions();
	}

	@Override
	public List<Map<String, Object>> getMinorRegions(int majorRegionIdx) throws Exception {
		return coachMapper.getMinorRegions(majorRegionIdx);
	}

	@Override
	public Integer getUsersIdx(String login_id) throws Exception {
		return coachMapper.getUsersIdx(login_id);
	}

	@Override
	public Integer coachJoin(CoachDTO cdto) throws Exception {
		return coachMapper.coachJoin(cdto);
	}

	@Override
	public CoachDTO getCoachInfo(int users_idx) throws Exception {
		
		return coachMapper.getCoachInfo(users_idx);
	}

	@Override
	public CoachDTO getCoachByKakaoKey(String kakaoKey) throws Exception {
		return coachMapper.getCoachByKakaoKey(kakaoKey);
	}

	@Override
	public int connectKakao(HashMap<String, Object> conKakao) throws Exception {
		return coachMapper.connectKakao(conKakao);
	}

	@Override
	public boolean emailCheck(String email) throws Exception {
		int result=coachMapper.emailCheck(email);
		return result>0?true:false;
	}

	@Override
	public Integer activateCoach(int user_idx) throws Exception {
		return coachMapper.activateCoach(user_idx);
	}

	@Override
	@Transactional
	public void saveCoachDetails(int user_idx, int myMinorCate, int interMinorCate, int myMajorRegion,
			int myMinorRegion, String myHashtags, String interHashtags) throws Exception {
		int coach_idx=coachMapper.getCoachInfo(user_idx).getCoach_idx();
		//제공분야 및 활동지역
		HashMap map=new HashMap();
		map.put("coach_idx", coach_idx);
		map.put("myMinorCate", myMinorCate);
		map.put("interMinorCate", interMinorCate);
		map.put("myMajorRegion", myMajorRegion);
		map.put("myMinorRegion", myMinorRegion);
		coachMapper.saveMyMinorCate(map);
		coachMapper.saveInterMinorCate(map);
		coachMapper.saveMyRegions(map);
		
		// 1. DB의 기존 태그 가져오기
		HashSet<String> allHashtags = new HashSet<>(coachMapper.allHashtags());

		// 2. mytags 처리
		HashSet<String> mytags = new HashSet<>();
		for (String s : myHashtags.split("#")) {
		    String t = s.trim();
		    if (!t.isEmpty()) mytags.add("#"+t); // 빈 값과 공백 제거 후 추가
		}
		
		HashSet<String> mytags_original = new HashSet<>(mytags); // 원본 복사본 생성
		mytags.removeAll(allHashtags);

		// 저장할 태그가 있을 때만 실행
		if (!mytags.isEmpty()) {
		    coachMapper.saveHashtags(mytags);
		    allHashtags.addAll(mytags); // 다음 비교를 위해 전체 목록 업데이트
		}
		// 3. intertags 처리
		HashSet<String> intertags = new HashSet<>();
		for (String s : interHashtags.split("#")) {
		    String t = s.trim();
		    if (!t.isEmpty()) intertags.add("#"+t);
		}
		HashSet<String> intertags_original = new HashSet<>(intertags); // 원본 복사본 생성
		intertags.removeAll(allHashtags);

		if (!intertags.isEmpty()) {
		    coachMapper.saveHashtags(intertags);
		}
		
		List<Integer> mytags_idx = coachMapper.getHashtagIdxs(mytags_original);
		List<Integer> intertags_idx = coachMapper.getHashtagIdxs(intertags_original);
		
		if(!mytags_idx.isEmpty()) {
			coachMapper.myHashtagMapping(user_idx, mytags_idx);
		}
		if(!intertags_idx.isEmpty()) {
			coachMapper.interHashtagMapping(user_idx, intertags_idx);
		}
		
	}

	@Override
	public boolean emailCompanyCheck(String email) throws Exception {
		int result=coachMapper.emailCompanyCheck(email);
		return result>0?true:false;
	}

	@Override
	public Integer pwdChange(String login_id, String password) throws Exception {
		HashMap<String, Object> map=new HashMap<>();
		map.put("login_id", login_id);
		map.put("password", com.dacoach.javasecure.JavaDataSecureModule.getSHA256(password));
		int result=coachMapper.pwdChange(map);
		return result;
	}

	
	@Override
    @Transactional
    public void updateMyInfo(
            CoachDTO dto,
            MultipartFile uploadPhoto,
            MultipartFile uploadVideo,
            int myMinorCate,
            int interMinorCate,
            int myMajorRegion,
            int myMinorRegion,
            String myHashtags,
            String interHashtags
    ) throws Exception {

        // 기존 코치 정보
        CoachDTO origin = coachMapper.getCoachInfo(dto.getUser_idx());
        if (origin == null) {
            throw new IllegalStateException("코치 정보가 존재하지 않습니다.");
        }

        // 파일 처리
        String newPhotoPath = origin.getPhoto();
        String newVideoPath = origin.getVideo();

        if (uploadPhoto != null && !uploadPhoto.isEmpty()) {
            // 기존 파일 삭제
            FileUpload.deleteFile(origin.getPhoto());
            newPhotoPath = FileUpload.saveFile(uploadPhoto, "coach/profile");
        }
        if (uploadVideo != null && !uploadVideo.isEmpty()) {
            FileUpload.deleteFile(origin.getVideo());
            newVideoPath = FileUpload.saveFile(uploadVideo, "coach/video");
        }

        dto.setPhoto(newPhotoPath);
        dto.setVideo(newVideoPath);

        // 코치 기본정보 update
        int updated = coachMapper.updateCoachInfo(dto);
        if (updated <= 0) {
            throw new RuntimeException("코치 기본정보 수정 실패");
        }

        // 매핑 테이블 삭제
        int coach_idx = origin.getCoach_idx();

        coachMapper.deleteProvideByCoachIdx(coach_idx);
        coachMapper.deleteInterestByCoachIdx(coach_idx);
        coachMapper.deleteRegionByCoachIdx(coach_idx);

        coachMapper.deleteMyHashtagMapping(dto.getUser_idx());
        coachMapper.deleteInterHashtagMapping(dto.getUser_idx());

        // 다시 저장
        saveCoachDetails(dto.getUser_idx(),
                myMinorCate, interMinorCate,
                myMajorRegion, myMinorRegion,
                myHashtags == null ? "" : myHashtags,
                interHashtags == null ? "" : interHashtags
        );
    }
	
	@Override
    public Map<String, Object> getMyProvideField(int user_idx) throws Exception {
        return coachMapper.getMyProvideField(user_idx);
    }

    @Override
    public Map<String, Object> getMyInterestField(int user_idx) throws Exception {
        return coachMapper.getMyInterestField(user_idx);
    }

    @Override
    public Map<String, Object> getMyRegion(int user_idx) throws Exception {
        return coachMapper.getMyRegion(user_idx);
    }

    @Override
    public List<String> getMyHashtags(int user_idx) throws Exception {
        return coachMapper.getMyHashtags(user_idx);
    }

    @Override
    public List<String> getInterHashtags(int user_idx) throws Exception {
        return coachMapper.getInterHashtags(user_idx);
    }

	@Override
	public Integer likeCoach(int login_idx, int targer_idx) throws Exception {
		HashMap<String, Object> map=new HashMap<>();
		map.put("login_idx", login_idx);
		map.put("target_idx", targer_idx);
		int result=coachMapper.likeCoach(map);
		return result;
	}
	@Override
	public Integer unlikeCoach(int login_idx, int targer_idx) throws Exception {
		HashMap<String, Object> map=new HashMap<>();
		map.put("login_idx", login_idx);
		map.put("target_idx", targer_idx);
		int result=coachMapper.unlikeCoach(map);
		return result;
	}
	
	@Override
	public List<CoachDTO> getPopularCoach() throws Exception {
		List<CoachDTO> list = coachMapper.getPopularCoach();

	    // 인기코치 5명 각각의 해시태그 채우기
		for (CoachDTO c : list) {
	        int userIdx = c.getUser_idx();

	        List<String> my = coachMapper.getMyHashtags(userIdx);
	        List<String> inter = coachMapper.getInterHashtags(userIdx);

	        // 최대 2개만
	        if (my != null && my.size() > 2) {
	            my = my.subList(0, 2);
	        }
	        if (inter != null && inter.size() > 2) {
	            inter = inter.subList(0, 2);
	        }

	        c.setHashtags(my);
	        c.setInterhashtags(inter);
	    }
	    return list;
	}

	@Override
	public List<CertDTO> getCoachCertList(int user_idx) throws Exception {
		List<CertDTO> allLists=coachMapper.getCoachCertList(user_idx);
		return allLists;
	}
}
