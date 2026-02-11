package com.dacoach.controller;

import java.util.ArrayList;
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
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.company.CompanyProvideDTO;
import com.dacoach.model.company.CompanyRegionDTO;
import com.dacoach.model.users.UsersDTO;
import com.dacoach.service.company.CompanyService;
import com.dacoach.service.file.FileUpload;
import com.dacoach.service.membership.MembershipService;

@Controller
public class CompanyController {

    private final WebSocketConfig webSocketConfig;

	@Autowired
	private CompanyService companyService;
	@Autowired MembershipService membershipService;
	private HashMap<String, Object> m;

	public CompanyController(WebSocketConfig webSocketConfig) {
		m = new HashMap<String, Object>();
		this.webSocketConfig = webSocketConfig;
	}

	@GetMapping("/companyJoin")
	public String joinForm() {

		return "/company/join/companyJoin";
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
		List<Map<String, Object>> field = null;
		List<Map<String, Object>> region = null;
		try {
			int companyNum=companyService.getCompanyNum(userIdx);
			field = companyService.fieldTeg();
			region = companyService.regionTeg();
			System.out.println(companyService.getCompanyRegion(companyNum));
			if(companyService.regionCheck(companyNum))
			mav.addObject("regionList",companyService.getCompanyRegion(companyNum));
			
			mav.addObject("field_con", field);
			mav.addObject("region_con", region);
			mav.addObject("company_num",companyService.getCompanyNum(companyNum));
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
		String msg=null;
		try {
			int addRegionVal=companyService.addRegion(dto);
			msg=addRegionVal>0?minorName:null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseEntity<String> re=new ResponseEntity<>(msg,HttpStatus.OK);
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
	
	@PostMapping("/company/profile/companyProfileForm")
	public ModelAndView companyProfileForm(Integer idx){
		ModelAndView mav=new ModelAndView();
		
		try {
			Map<String,Object> map=companyService.companyProfile(idx);
			int companyIdx=Integer.parseInt(String.valueOf(map.get("COMPANY_IDX")));
			List<Map<String,Object>> region=companyService.getCompanyRegion(companyIdx);
			System.out.println(region);
			mav.addObject("profile",map);
			mav.addObject("regionList",region);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mav.setViewName("/company/profile/companyProfileForm");
		return mav;
	}
	

}
