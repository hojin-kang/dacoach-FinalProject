package com.dacoach.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.dacoach.config.WebSocketConfig;
import com.dacoach.kakaopay.PayDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.company.CompanyProvideDTO;
import com.dacoach.model.company.CompanyRegionDTO;
import com.dacoach.model.membership.MembershipDTO;
import com.dacoach.model.notification.NotificationDTO;
import com.dacoach.model.users.UsersDTO;
import com.dacoach.service.chat.ChatService;
import com.dacoach.service.company.CompanyService;
import com.dacoach.service.file.FileUpload;
import com.dacoach.service.membership.MembershipService;
import com.dacoach.service.notification.NotificationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CompanyController {

    private final WebSocketConfig webSocketConfig;

	@Autowired
	private CompanyService companyService;
	@Autowired MembershipService membershipService;
	private HashMap<String, Object> m;
	
	@Autowired
    private NotificationService notificationService;
	
	@Autowired
	private ChatService chatService;
	
	public CompanyController(WebSocketConfig webSocketConfig) {
		m = new HashMap<String, Object>();
		this.webSocketConfig = webSocketConfig;
	}

	@GetMapping("/companyJoin")
	public String joinForm() {

		return "/company/join/companyJoin";
	}
	@GetMapping("/company/mypage/myPayment")
	public ModelAndView myPaymentList(HttpSession session) {
	    ModelAndView mav = new ModelAndView();

	    Integer user_idx = (Integer) session.getAttribute("user_idx");
	    if (user_idx == null || user_idx == 0) {
	        mav.setViewName("/needLogin");
	        return mav;
	    }

	    List<Map<String,Object>> payList = null;

	    try {
	       payList = companyService.getPayHistory(user_idx);
	        
	        mav.addObject("payList", payList);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    mav.setViewName("/company/profile/myPayment");
	    return mav;
	}
	
	@GetMapping("/company/mypage")
	public ModelAndView mypage(HttpSession session) {
		
		ModelAndView mav=new ModelAndView();
		if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			mav.setViewName("/needLogin");
			return mav;
		}
		
		try {
			MembershipDTO mdto=membershipService.userMembershipInfo((Integer)session.getAttribute("user_idx"));
			String membership=mdto.getMember_detail_idx()==1?"일반":"프리미엄";
			mav.addObject("membership",membership);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mav.addObject("user_name",session.getAttribute("user_name"));
		mav.setViewName("/company/profile/mypage");
		return mav;
	}
	
	@GetMapping("/company/mypage/myInfo")
	public ModelAndView myInfo(HttpSession session) {
		ModelAndView mav=new ModelAndView();
		int userIdx=(int)session.getAttribute("user_idx");
		int companyIdx=(int)session.getAttribute("company_idx");
		try {
			CompanyDTO companyDto=companyService.getCompanyInfo(userIdx);
			if(companyDto.getPhoto()==null) {
				companyDto.setPhoto("/img/profile/defaultProfile.jpg");
			}else {
				String newPhoto="/uploads/"+companyDto.getPhoto();
				companyDto.setPhoto(newPhoto);
			}
			List<Map<String,Object>> regionList=companyService.getCompanyRegion(companyIdx);
			Map<String,Object> provideDto=companyService.getCompanyProvide(companyIdx);
			
			mav.addObject("fieldTag", companyService.fieldTeg());
			mav.addObject("regionTag",companyService.regionTeg());
			mav.addObject("user", (String)session.getAttribute("user_name"));
			mav.addObject("company",companyDto);
			mav.addObject("regions", regionList);
			mav.addObject("provide", provideDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mav.setViewName("/company/profile/myInfo");
		return mav;
	}
	
	@PostMapping("/company/mypage/myinfoUp")
	public ModelAndView myinfoUp(@RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
			CompanyProvideDTO provideDto,
			CompanyDTO companyDto,
			HttpSession session) {
		ModelAndView mav=new ModelAndView();
		companyDto.setUser_idx((int)session.getAttribute("user_idx"));
		provideDto.setCompany_idx((int)session.getAttribute("company_idx"));
		if (photoFile != null && !photoFile.isEmpty()) {
			FileUpload.deleteFile(companyDto.getPhoto());
			String photoPath = FileUpload.saveFile(photoFile, "company/photo");			
			companyDto.setPhoto(photoPath);								
		}else {
			
			companyDto.setPhoto((String)session.getAttribute("photo"));
		}
		try {
			boolean result=companyService.companyUp(companyDto, provideDto);
			if(!result) {

				mav.addObject("msg", "변경사항 저장 중 문제가 발생하였습니다.");
			}else {
				session.removeAttribute("photo");
				session.setAttribute("photo", companyDto.getPhoto());
				mav.addObject("msg", "변경사항 저장이 완료되었습니다.");			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mav.addObject("url","/company/mypage/myInfo" );
		mav.setViewName("alert");
		return mav;
	}
	
	@PostMapping("company/join/loginInfoOK")
	public ModelAndView LoginInfoOk(UsersDTO dto) {
		try {
			int idx = companyService.joinOk(dto);
			int midx=membershipService.defaultMembership(companyService.getUserIdx(dto.getLogin_id()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("login_id", dto.getLogin_id());
		mav.setViewName("forward:/company/profile/companyInfo");
		return mav;
	}
	
	@RequestMapping("/company/profile/companyInfo")
	public ModelAndView companyInfo() {

		ModelAndView mav = new ModelAndView();

		mav.setViewName("/company/profile/companyInfo");
		return mav;
	}

	@PostMapping("/company/profile/companyInfoOk")
	public ModelAndView companyInfoOk(CompanyDTO companyDto,
										CertDTO certDto,
										String login_id,
							@RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
							@RequestParam(value = "certFile", required = false) MultipartFile certFile) {
		ModelAndView mav = new ModelAndView();

		try {
			int userIdx=companyService.getUserIdx(login_id);
			if (photoFile != null && !photoFile.isEmpty()) {
				String photoPath = FileUpload.saveFile(photoFile, "company/photo");			
				companyDto.setPhoto(photoPath);								
			}
			if (certFile != null && !certFile.isEmpty()) {
			String certPath = FileUpload.saveFile(certFile, "company/cert");
			certDto.setCert_file(certPath);			
			}
			companyDto.setUser_idx(companyService.getUserIdx(login_id));
			certDto.setUser_idx(companyService.getUserIdx(login_id));
			int infoResult = companyService.companyInfo(companyDto);
			certDto.setCert_name("사업자등록증");
			int certResurt = companyService.insertcert(certDto);
			if(infoResult<=0||certResurt<=0) {
				mav.addObject("msg","오류가 발생했습니다 다시 시도해주세요");
				mav.addObject("url","/");
				mav.setViewName("alert");
				return mav;
			}
						
			mav.setViewName("redirect:/company/profile/profileForm?userIdx="+userIdx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return mav;
	}

	@GetMapping("/company/profile/profileForm")
	public ModelAndView profileForm(Integer userIdx) {
		ModelAndView mav = new ModelAndView();

		try {
			int companyNum=companyService.getCompanyNum(userIdx);
			List<Map<String, Object>>field = companyService.fieldTeg();
			List<Map<String, Object>> region = companyService.regionTeg();
			
			if(companyService.regionCheck(companyNum))
			mav.addObject("regionList",companyService.getCompanyRegion(companyNum));
			
			mav.addObject("field_con", field);
			mav.addObject("region_con", region);
			mav.addObject("company_num",companyNum);
			mav.setViewName("/company/profile/profileForm");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mav;
	}
	@PostMapping("/company/profile/provideOk")
	public ModelAndView provideOk(CompanyProvideDTO dto) {
		ModelAndView mav=new ModelAndView();
		try {
			int result=companyService.provideOk(dto);
			String msg=result>0?"회원가입 감사드립니다":"회원가입 실패하였습니다 관리자에게 문의부탁드립니다";
			mav.addObject("msg",msg);
			mav.setViewName("/company/join/companyJoinOkMsg");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mav;
	}
	@GetMapping("/api/company/getMinorRegion")
	public ResponseEntity<List<Map<String, Object>>> getMinorRegion(Integer regionIdx) {
		List<Map<String, Object>> minorRegion = null;
		try {
			minorRegion = companyService.getRegionTag(regionIdx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseEntity<List<Map<String, Object>>> re = new ResponseEntity<List<Map<String, Object>>>(minorRegion,
				HttpStatus.OK);
		return re;
	}
	
	@PostMapping("/api/company/addRegion")
	public ResponseEntity<String> addRegion(@RequestBody CompanyRegionDTO dto,String minorName){
		String msg="";
		try {
			
			
			List<CompanyRegionDTO> checkDtos=companyService.getCompanyAllRegion(dto.getCompany_idx());
			CompanyRegionDTO  checkDto=new CompanyRegionDTO();
			for(int i=0;i<checkDtos.size();i++) {
				checkDto=checkDtos.get(i);
				if(checkDto.getMajor_region_idx()==dto.getMajor_region_idx()
						&&checkDto.getMinor_region_idx()==dto.getMinor_region_idx()) {
					msg="중복";
				}
			}
			if(!msg.equals("중복")) {
				int addRegionVal=companyService.addRegion(dto);
				msg=addRegionVal>0?minorName:null;
				}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseEntity<String> re=new ResponseEntity<>(msg,HttpStatus.OK);
		return re;
	}
	
	@PostMapping("/api/company/regionDel")
	public ResponseEntity<String> regionDel(@RequestBody CompanyRegionDTO regionDto){
		
		
		String msg="";
		try {
			
			int result=companyService.regionDel(regionDto);	
			msg=result>0?"삭제되었습니다":"주소 삭제에 실패했거나 데이터가 없습니다.";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseEntity<String> re =new ResponseEntity<String>(msg,HttpStatus.OK);
		
		return re;
	}
	@GetMapping("/api/company/getMinorField") 
	public ResponseEntity<List<Map<String, Object>>> getMinorField(Integer fieldIdx){
		List<Map<String,Object>> fields=null;
		
		try {
			fields=companyService.getMinorField(fieldIdx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseEntity<List<Map<String, Object>>> re =new ResponseEntity<List<Map<String,Object>>>(fields,HttpStatus.OK);
		return re;
	}
	
	@RequestMapping("/company/profile/companyProfileForm")
	public ModelAndView companyProfileForm(Integer idx,@RequestParam(value="fromName",defaultValue = "noParam") String fromName){
		ModelAndView mav=new ModelAndView();
		System.out.println(idx);
		Calendar now=Calendar.getInstance();
		int year=now.get(Calendar.YEAR);
		int month=now.get(Calendar.MONTH)+1;
		int day=now.get(Calendar.DATE);
		String strDate=""+year+"-"+month+"-"+day;
		Date nowDay=Date.valueOf(strDate);
		Map<String,Object> classMap=new HashMap<>();
		try {
			Map<String,Object> map=null;
			if(fromName.equalsIgnoreCase("banner")) {
				int user_idx=companyService.memberToUser(idx);
				map=companyService.companyProfile(user_idx);
			}else {
				map=companyService.companyProfile(idx);
			}
			
			
			int userIdx=Integer.parseInt(String.valueOf(map.get("USER_IDX")));
			classMap.put("idx", userIdx);
			classMap.put("today", nowDay);
			
			int companyIdx=Integer.parseInt(String.valueOf(map.get("COMPANY_IDX")));
			List<Map<String,Object>> classList=companyService.getProfileClass(classMap);
			List<Map<String,Object>> region=companyService.getCompanyRegion(companyIdx);
			String rating=companyService.getRating(companyIdx);
			mav.addObject("rating", rating);
			mav.addObject("classList",classList);
			mav.addObject("profile",map);
			mav.addObject("regionList",region);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mav.setViewName("/company/profile/companyProfileForm");
		return mav;
	}
	
	@GetMapping("/company/notification/open")
	public String openNoti(int noti_idx, HttpSession session) {
	    if (session.getAttribute("user_idx") == null || (Integer) session.getAttribute("user_idx") == 0) {
	        return "redirect:/needLogin";
	    }
	    int user_idx = (Integer) session.getAttribute("user_idx");

	    // 내 알림 맞는지 확인용 조회 (새로 추가)
	    NotificationDTO n = notificationService.getNotiForUser(noti_idx, user_idx);
	    if (n == null) return "redirect:/notification";

	    String type = n.getNoti_type();
	    int provider = n.getProvider_idx();
	    String url="";
	    switch (type) {
	    
	    case "MEMBERSHIP": url = "redirect:/membership/membershipForm"; break;
	   
	    }
	    notificationService.notiDelete(noti_idx);
	    return url;
	}
	
	public int easyNotifi(int Receiver_idx,int provider_idx,String noti_type,String content) {
		
		NotificationDTO nt=new NotificationDTO();
		nt.setReceiver_idx(Receiver_idx);
		nt.setProvider_idx(provider_idx);
		nt.setNoti_type(noti_type);
		nt.setContent(content);
		
		return notificationService.insertNotification(nt);
	}
}
