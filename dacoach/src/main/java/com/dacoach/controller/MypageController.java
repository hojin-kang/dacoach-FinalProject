package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.sql.Date;

import com.dacoach.service.qna.QnaService;
import com.dacoach.service.review.ReviewService;
import com.dacoach.service.token.TokenService;
import com.dacoach.model.challenge.ChallengeDTO;
import com.dacoach.kakaopay.PayDTO;
import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.likes.LikesClassDTO;
import com.dacoach.model.likes.LikesUserDTO;
import com.dacoach.model.notification.NotificationDTO;
import com.dacoach.model.qna.QnaDTO;
import com.dacoach.model.token.TokenHistoryDTO;
import com.dacoach.service.challenge.ChallengeService;
import com.dacoach.service.coach.CoachService;
import com.dacoach.service.company.CompanyService;
import com.dacoach.service.file.FileUpload;
import com.dacoach.service.likes.LikesService;
import com.dacoach.service.mypage.MypageService;
import com.dacoach.service.notification.NotificationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

	@Autowired
    private QnaService qnaService;
	
	@Autowired
	private MypageService mypageService;
	
	@Autowired
	private LikesService likesService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private CoachService coachService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ChallengeService challengeService;
	
	@Autowired
	private NotificationService notificationService;
	
	

	@GetMapping("/mypage")
	public ModelAndView mypageMain(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			System.out.println(session.getAttribute("user_idx"));
			mav.setViewName("/needLogin");
			return mav;
		}
		int user_idx = (Integer)session.getAttribute("user_idx");
		
		String user_nickname = "";
		String user_rank = "";
		
		try {
			Map users_info = mypageService.getUserInfo(user_idx);
			if (users_info != null) {
			    user_nickname = (String) users_info.get("NICKNAME");
			    user_rank = (String) users_info.get("RANKNAME");
			}
			mav.addObject("withdrawReasons", mypageService.getWithdrawReasons());
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.addObject("user_nickname", user_nickname);
		mav.addObject("user_rank", user_rank);
		
	    
		mav.setViewName("/coach/mypage/mypage");
		return mav;
	}
	
	@PostMapping("/withdraw")
	public String withdraw(
	        @RequestParam("reason_type_idx") int reasonTypeIdx,
	        HttpSession session
	) {
	    Integer user_idx = (Integer) session.getAttribute("user_idx");
	    if (user_idx == null || user_idx == 0) return "redirect:/needLogin";

	    mypageService.withdrawUser(user_idx, reasonTypeIdx);

	    // 세션 만료(로그아웃)
	    session.invalidate();
	    return "redirect:/";
	}
	
	// 마이페이지 > 개인정보 수정 페이지
    @GetMapping("/myInfo")
    public ModelAndView myInfo(HttpSession session) {
        ModelAndView mav = new ModelAndView();

        if (session.getAttribute("user_idx") == null || (Integer) session.getAttribute("user_idx") == 0) {
            mav.setViewName("/needLogin");
            return mav;
        }

        int user_idx = (Integer) session.getAttribute("user_idx");

        try {
            // 셀렉트 옵션
            mav.addObject("majorFields", coachService.getMajorFields());
            mav.addObject("majorRegions", coachService.getMajorRegions());

            // 기본정보
            CoachDTO coachInfo = coachService.getCoachInfo(user_idx);
            mav.addObject("coachInfo", coachInfo);

            // 기존 매핑값
            mav.addObject("myField", coachService.getMyProvideField(user_idx));
            mav.addObject("interField", coachService.getMyInterestField(user_idx));
            mav.addObject("myRegion", coachService.getMyRegion(user_idx));

            // 기존 해시태그(리스트)
            mav.addObject("myTags", coachService.getMyHashtags(user_idx));
            mav.addObject("interTags", coachService.getInterHashtags(user_idx));

        } catch (Exception e) {
            e.printStackTrace();
        }

        mav.setViewName("/coach/mypage/myInfo");
        return mav;
    }

    // 수정 저장 (가입 coachJoin이랑 거의 같은 방식)
    @PostMapping("/myInfoUpdate")
    public ModelAndView myInfoUpdate(
            HttpSession session,
            @RequestParam(value = "uploadPhoto", required = false) MultipartFile uploadPhoto,
            @RequestParam(value = "uploadVideo", required = false) MultipartFile uploadVideo,

            @RequestParam("nickname") String nickname,
            @RequestParam(value = "intro", required = false) String intro,
            @RequestParam(value="birth_date", required=false) String birth_date,
            @RequestParam("phone") String phone,

            @RequestParam("myMinorCate") int myMinorCate,
            @RequestParam("interMinorCate") int interMinorCate,
            @RequestParam("myMajorRegion") int myMajorRegion,
            @RequestParam("myMinorRegion") int myMinorRegion,
            @RequestParam(value = "myHashtags", required = false, defaultValue = "") String myHashtags,
            @RequestParam(value = "interHashtags", required = false, defaultValue = "") String interHashtags
    ) {
        ModelAndView mav = new ModelAndView();

        if (session.getAttribute("user_idx") == null || (Integer) session.getAttribute("user_idx") == 0) {
            mav.setViewName("/needLogin");
            return mav;
        }

        int user_idx = (Integer) session.getAttribute("user_idx");

        try {
        	java.sql.Date birthSqlDate = null;
        	if (birth_date != null && !birth_date.isBlank()) {
        	    birthSqlDate = java.sql.Date.valueOf(birth_date); // "yyyy-MM-dd" 전용
        	}
        	
        	
            CoachDTO dto = new CoachDTO();
            dto.setUser_idx(user_idx);
            dto.setNickname(nickname);
            dto.setIntro(intro);
            dto.setBirth_date(birthSqlDate);
            dto.setPhone(phone);

            // service에서: 기존 파일 유지/교체 + 기본정보 update + 매핑 싹 갱신
            coachService.updateMyInfo(dto, uploadPhoto, uploadVideo,
                    myMinorCate, interMinorCate, myMajorRegion, myMinorRegion, myHashtags, interHashtags);
            
            // 여기서 DB 최신값으로 세션 photo 갱신
            CoachDTO refreshed = coachService.getCoachInfo(user_idx);
            if (refreshed != null) {
                session.setAttribute("photo",
                        (refreshed.getPhoto() == null || refreshed.getPhoto().isBlank()) ? null : refreshed.getPhoto());
            }

            mav.addObject("msg", "개인정보 수정이 완료되었습니다.");
            mav.addObject("url", "/myInfo");
            mav.setViewName("alert");
            return mav;

        } catch (Exception e) {
            e.printStackTrace();
            mav.addObject("msg", "오류가 발생했습니다. 다시 시도해주세요.");
            mav.addObject("url", "/myInfo");
            mav.setViewName("alert");
            return mav;
        }
    }
	
	@GetMapping("/myHeart")
	  public ModelAndView myHeart(HttpSession session) throws Exception {
	    ModelAndView mav = new ModelAndView();

	    Integer userIdx = (Integer) session.getAttribute("user_idx");
	    if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			mav.setViewName("/needLogin");
			return mav;
		}

	    List<LikesUserDTO> likedCoaches = likesService.getLikedCoaches(userIdx);
	    List<LikesClassDTO> likedClasses = likesService.getLikedClasses(userIdx);

	    mav.addObject("likedCoaches", likedCoaches);
	    mav.addObject("likedClasses", likedClasses);

	    mav.addObject("likedCoachCount", likedCoaches == null ? 0 : likedCoaches.size());
	    mav.addObject("likedClassCount", likedClasses == null ? 0 : likedClasses.size());

	    mav.setViewName("coach/mypage/myHeart");
	    return mav;
	  }
	
	@GetMapping("/myReview")
	public ModelAndView myReviewList(HttpSession session) {
	    ModelAndView mav = new ModelAndView();
	    if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
	        mav.setViewName("/needLogin");
	        return mav;
	    }

	    int user_idx = (Integer) session.getAttribute("user_idx");

	    String userType = reviewService.getUserType(user_idx);
	    mav.addObject("userType", userType);

	    if ("COACH".equals(userType)) {
	        mav.addObject("writtenCoachReviews", reviewService.getWrittenCoachReviews(user_idx));
	        mav.addObject("writtenClassReviews", reviewService.getWrittenClassReviews(user_idx));
	        mav.addObject("canWriteReview", true);
	    } else {
	        mav.addObject("writtenCoachReviews", Collections.emptyList());
	        mav.addObject("writtenClassReviews", Collections.emptyList());
	        mav.addObject("canWriteReview", false);
	    }

	    mav.addObject("receivedCoachReviews", reviewService.getReceivedCoachReviews(user_idx));

	    mav.addObject("receivedClassReviews", Collections.emptyList());

	    mav.setViewName("/coach/mypage/myReview");
	    return mav;
	}

	
	
	
	@GetMapping("/myPayment")
	public ModelAndView myPaymentList(HttpSession session) {
	    ModelAndView mav = new ModelAndView();

	    Integer user_idx = (Integer) session.getAttribute("user_idx");
	    if (user_idx == null || user_idx == 0) {
	        mav.setViewName("/needLogin");
	        return mav;
	    }

	    List<PayDTO> payList = null;

	    try {
	        payList = tokenService.getPayHistory(user_idx);
	        
	        for (PayDTO p : payList) {
	            String label = switch (p.getPay_type()) {
	                case "TOKEN" -> "닭꼬치";
	                case "CLASS" -> "클래스";
	                case "MEMBERSHIP" -> "멤버십";
	                default -> p.getPay_type();
	            };
	            p.setPay_type(label);
	        }
	        
	        mav.addObject("payList", payList);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    mav.setViewName("/coach/mypage/myPayment");
	    return mav;
	}
	
	@GetMapping("/notice")
	public String noticeList() {
		return "/coach/mypage/notice";
	}
	
	@GetMapping("/myQnaList")
	public ModelAndView myQnaList(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			mav.setViewName("/needLogin");
			return mav;
		}
		int user_idx = (Integer)session.getAttribute("user_idx");
		try {
			mav.addObject("qnaList", qnaService.myQnaList(user_idx));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mav.setViewName("/coach/mypage/myQna");
		
		return mav;
	}
	
	@GetMapping("/myQnaDetail")
	public ModelAndView myQnaDetail(@RequestParam(value="qna_idx", defaultValue = "0") Integer qna_idx, HttpSession session) {
	    ModelAndView mav = new ModelAndView();
	    if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			mav.setViewName("/needLogin");
			return mav;
		}
	    int user_idx = (Integer) session.getAttribute("user_idx");
	    Map<String, Object> data = null;
		try {
			data = qnaService.myQnaDetailWithAnswer(qna_idx, user_idx);
		} catch (Exception e) {
			e.printStackTrace();
		}

	    mav.addObject("qna", data.get("qna"));
	    mav.addObject("answer", data.get("answer"));
	    mav.setViewName("/coach/mypage/myQnaDetail");
	    return mav;
	}
	
	@GetMapping("/myQnaForm")
	public String myQnaForm(HttpSession session) {
		if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			return "/needLogin";
		}
		return "/coach/mypage/myQnaForm";
	}
	
	@PostMapping("/myQnaNew")
	public ModelAndView myQnaNew(QnaDTO qdto, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			mav.setViewName("/needLogin");
			return mav;
		}
		qdto.setUser_idx((Integer) session.getAttribute("user_idx"));
		int result = 0;
		try {
			result = qnaService.myQnaNew(qdto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result > 0) {
	        mav.setViewName("redirect:/myQnaList");
	    } else {
	        mav.setViewName("/coach/mypage/myQnaForm");
	    }
		return mav;
	}
	@GetMapping("/cert")
	public ModelAndView certPage(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			mav.setViewName("/alert");
			mav.addObject("msg", "로그인이 필요합니다.");
			mav.addObject("url", "/login");
			return mav;
		}
		mav.setViewName("/coach/mypage/cert");
		return mav;
	}
	@PostMapping("/cert")
	public ModelAndView certPage(CertDTO dto, HttpSession session, MultipartFile certFile) {
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			mav.setViewName("/alert");
			mav.addObject("msg", "로그인이 필요합니다.");
			mav.addObject("url", "/login");
			return mav;
		}
		int result = 0;
		try {
			dto.setCert_file(FileUpload.saveFile(certFile,"coach/cert"));
			result = companyService.insertcert(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result > 0) {
			mav.addObject("msg", "정상적으로 제출되었습니다.");
			mav.addObject("url", "/myCert");
	        mav.setViewName("alert");
	    } else {
	    	mav.addObject("msg", "다시 시도해주세요");
			mav.addObject("url", "/cert");
	        mav.setViewName("alert");
	    }
		return mav;
	}
	@GetMapping("/myCert")
	public ModelAndView myCertList(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			mav.setViewName("/alert");
			mav.addObject("msg", "로그인이 필요합니다.");
			mav.addObject("url", "/login");
			return mav;
		}
		List<CertDTO> certificatedList = new ArrayList<>();
		List<CertDTO> pendingList = new ArrayList<>();
		List<CertDTO> rejectedList = new ArrayList<>();
		
		List<CertDTO> allList = new ArrayList<>();
		
		try {
			allList=coachService.getCoachCertList((Integer)session.getAttribute("user_idx"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<allList.size();i++) {
			CertDTO dto = allList.get(i);
			if(dto.getCert_status().equals("승인")) {
				certificatedList.add(dto);
			}else if(dto.getCert_status().equals("대기")) {
				pendingList.add(dto);
			}else if(dto.getCert_status().equals("반려")) {
				rejectedList.add(dto);
			}
		}
		mav.addObject("certificatedList", certificatedList);
		mav.addObject("pendingList", pendingList);
		mav.addObject("rejectedList", rejectedList);
		mav.setViewName("/coach/mypage/myCert");
		return mav;
	}
	@GetMapping("/myChallenge")
	public ModelAndView myChallenge(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			mav.addObject("msg", "로그인이 필요합니다.");
			mav.addObject("url", "/login");
			mav.setViewName("/alert");
			return mav;
		}
		int user_idx = (Integer)session.getAttribute("user_idx");
		
		try {
			List<ChallengeDTO> challengeList = challengeService.getAllChallenges(user_idx);
			//최초접속 시 기본챌린지 삽입
			if(challengeList == null || challengeList.size() == 0) {
				challengeService.insertDefaultChallenges(user_idx);
				challengeList = challengeService.getAllChallenges(user_idx);
			}
			//현재 유저 포인트, 좋아요, 작성한 리뷰, 매칭 수 가져오기
			HashMap<String, Object> scores=challengeService.getScores(user_idx);
			int likes=((Number) scores.get("LIKES")).intValue();
			int points=((Number) scores.get("POINT_SCORE")).intValue();
			int reviews=((Number) scores.get("REVIEWS")).intValue();
			int matches=((Number) scores.get("MATCHES")).intValue();
			//과제 달성여부 체크 및 db업데이트
			for(int i=0;i<challengeList.size();i++) {
				if(challengeList.get(i).getType().equals("POINT")&&challengeList.get(i).getAchieve().equals("N")&&challengeList.get(i).getQuantity()<=points) {
					challengeList.get(i).setUser_idx(user_idx);
					challengeService.achieveChallenge(challengeList.get(i));
					//토큰 지급
					int token=tokenService.giftToken(user_idx, (challengeList.get(i).getPrize())/10);
					if(token>0) {
						//지급 성공 시 알림
						NotificationDTO dto = new NotificationDTO();
						dto.setProvider_idx(1);
						dto.setReceiver_idx(user_idx);
						dto.setNoti_type("CHALLENGE");
						dto.setContent(challengeList.get(i).getName()+" 챌린지를 달성하였습니다! 닭꼬치 "+(challengeList.get(i).getPrize())/10+"개 지급 완료");
						notificationService.insertNotification(dto);
					}
					
					mav.addObject("msg", challengeList.get(i).getName()+" 챌린지를 달성하였습니다!");
					mav.addObject("url", "/myChallenge");
					mav.setViewName("/alert");
					return mav;
				}else if(challengeList.get(i).getType().equals("LIKE")&&challengeList.get(i).getAchieve().equals("N")&&challengeList.get(i).getQuantity()<=likes) {
					challengeList.get(i).setUser_idx(user_idx);
					challengeService.achieveChallenge(challengeList.get(i));
					//토큰 지급
					int token=tokenService.giftToken(user_idx, (challengeList.get(i).getPrize())/10);
					if(token>0) {
						//지급 성공 시 알림
						NotificationDTO dto = new NotificationDTO();
						dto.setProvider_idx(1);
						dto.setReceiver_idx(user_idx);
						dto.setNoti_type("CHALLENGE");
						dto.setContent(challengeList.get(i).getName()+" 챌린지를 달성하였습니다! 닭꼬치 "+(challengeList.get(i).getPrize())/10+"개 지급 완료");
						notificationService.insertNotification(dto);
					}
					mav.addObject("msg", challengeList.get(i).getName()+" 챌린지를 달성하였습니다!");
					mav.addObject("url", "/myChallenge");
					mav.setViewName("/alert");
					return mav;
				}else if(challengeList.get(i).getType().equals("REVIEW")&&challengeList.get(i).getAchieve().equals("N")&&challengeList.get(i).getQuantity()<=reviews) {
					challengeList.get(i).setUser_idx(user_idx);
					challengeService.achieveChallenge(challengeList.get(i));
					//토큰 지급
					int token=tokenService.giftToken(user_idx, (challengeList.get(i).getPrize())/10);
					if(token>0) {
						//지급 성공 시 알림
						NotificationDTO dto = new NotificationDTO();
						dto.setProvider_idx(1);
						dto.setReceiver_idx(user_idx);
						dto.setNoti_type("CHALLENGE");
						dto.setContent(challengeList.get(i).getName()+" 챌린지를 달성하였습니다! 닭꼬치 "+(challengeList.get(i).getPrize())/10+"개 지급 완료");
						notificationService.insertNotification(dto);
					}
					mav.addObject("msg", challengeList.get(i).getName()+" 챌린지를 달성하였습니다!");
					mav.addObject("url", "/myChallenge");
					mav.setViewName("/alert");
					return mav;
				}else if(challengeList.get(i).getType().equals("MATCH")&&challengeList.get(i).getAchieve().equals("N")&&challengeList.get(i).getQuantity()<=matches) {
					challengeList.get(i).setUser_idx(user_idx);
					challengeService.achieveChallenge(challengeList.get(i));
					//토큰 지급
					int token=tokenService.giftToken(user_idx, (challengeList.get(i).getPrize())/10);
					if(token>0) {
						//지급 성공 시 알림
						NotificationDTO dto = new NotificationDTO();
						dto.setProvider_idx(1);
						dto.setReceiver_idx(user_idx);
						dto.setNoti_type("CHALLENGE");
						dto.setContent(challengeList.get(i).getName()+" 챌린지를 달성하였습니다! 닭꼬치 "+(challengeList.get(i).getPrize())/10+"개 지급 완료");
						notificationService.insertNotification(dto);
					}
					mav.addObject("msg", challengeList.get(i).getName()+" 챌린지를 달성하였습니다!");
					mav.addObject("url", "/myChallenge");
					mav.setViewName("/alert");
					return mav;
				}
			}	
			
			//달성한 과제
			List<ChallengeDTO> achievedList = new ArrayList<>();
			for(int i=0; i<challengeList.size(); i++) {
				if(challengeList.get(i).getAchieve().equals("Y")) {
					achievedList.add(challengeList.get(i));
				}
			}
			//미달성한 과제
			List<ChallengeDTO> notAchievedList = new ArrayList<>();
			for(int i=0; i<challengeList.size(); i++) {
				if(challengeList.get(i).getAchieve().equals("N")) {
					notAchievedList.add(challengeList.get(i));
				}
			}
			
			mav.addObject("scores", scores);
			mav.addObject("challengeList", challengeList);
			mav.addObject("achievedList", achievedList);
			mav.addObject("notAchievedList", notAchievedList);
			mav.setViewName("/coach/mypage/myChallenge");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}
}
