package com.dacoach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.mapper.membership.MembershipMapper;
import com.dacoach.model.company.AdDTO;
import com.dacoach.model.membership.MembershipDTO;
import com.dacoach.service.file.FileUpload;
import com.dacoach.service.membership.MembershipService;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;

@Controller
@RequestMapping("/membership")
public class MembershipController {
	
	@Autowired
	private MembershipService membershipService;
	private HttpSession session;
	private MembershipDTO dto;
	public MembershipController(HttpSession se) {
		session=se;
	
	}
	
	@GetMapping("/membershipForm")
	public ModelAndView membershipForm() {
		
		ModelAndView mav=new ModelAndView();
		
		
		try {
			dto=membershipService.userMembershipInfo((Integer)session.getAttribute("user_idx"));
			mav.addObject("detail",membershipService.detail(dto.getMember_detail_idx()));
			mav.addObject("session",session);
			mav.addObject("dto",dto);
			mav.setViewName("/membership/membershipForm");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mav;
	}
	@GetMapping("/membershipUpForm")
	public ModelAndView membershipUpForm() {
		ModelAndView mav=new ModelAndView();
		
		try {
			mav.addObject("detail",membershipService.detailInfo(dto.getMember_detail_idx()));
			mav.addObject("session",session);
			mav.addObject("dto",dto);
			mav.setViewName("/membership/membershipUpdate");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mav;
		
	}
	@GetMapping("/membershipDown")
	public ModelAndView membershipDown(MembershipDTO dto)	{
		ModelAndView mav=new ModelAndView();
					
		try {
			dto.setUser_idx((Integer)session.getAttribute("user_idx"));
			if(this.dto.getStatus()!=null&& this.dto.getStatus().equals("취소")) {
				mav.addObject("msg","멤버십 해지 예정입니다");
			}else {
				membershipService.membershipDown(dto);
			mav.addObject("msg","멤버십 해지가 완료되었습니다");
			}
			mav.addObject("url","/membership/membershipForm");
			
			mav.setViewName("/alert");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return mav;
	}
	
	@GetMapping("/banner")
	public ModelAndView bannerForm() {
		ModelAndView mav=new ModelAndView();
		
		List<AdDTO> adDTO=null	;
		try {
			adDTO=membershipService.bannerSelect(dto.getMember_idx());
			mav.addObject("dto",adDTO);
			String action=adDTO==null||adDTO.size()==0?"add":"update";
			mav.addObject("action",action);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mav.setViewName("/membership/banner");
		return mav;
	}
	
	@PostMapping("/bannerAdd")
	public ModelAndView bannerAdd(AdDTO adDto,
			@RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
			String action) {
		ModelAndView mav=new ModelAndView();
		System.out.println(action);
		if (photoFile != null && !photoFile.isEmpty()) {
			
			try {
				dto=membershipService.userMembershipInfo((Integer)session.getAttribute("user_idx"));
			switch(action) {
			case "add":	
				String photoPath = FileUpload.saveFile(photoFile, "membership/banner");
				
				adDto.setPhoto(photoPath);
				adDto.setMember_idx(dto.getMember_idx());
				int result=membershipService.bannerAdd(adDto);
				if(result>0) {
					mav.addObject("msg","베너 등록 성공하였습니다");
					mav.addObject("url","/membership/membershipForm");					
				}else {
					mav.addObject("msg","베너 등록 실패하였습니다 잠시후 다시 시도해주세요");
					mav.addObject("url","/membership/membershipForm");
				}break;
			case "update":
					List<AdDTO> list=membershipService.bannerSelect(dto.getMember_idx());
					if(list!=null) {
					String upPhotoPath=list.get(0).getPhoto();
					FileUpload.deleteFile(upPhotoPath);
					upPhotoPath = FileUpload.saveFile(photoFile, "membership/banner");
					
					adDto.setPhoto(upPhotoPath);
					adDto.setMember_idx(dto.getMember_idx());
					int resultUp=membershipService.bannerUp(adDto);
					if(resultUp>0) {
						mav.addObject("msg","베너 등록 성공하였습니다");
						mav.addObject("url","/membership/membershipForm");					
					}else {
						mav.addObject("msg","베너 등록 실패하였습니다 잠시후 다시 시도해주세요");
						mav.addObject("url","/membership/membershipForm");
					}
				}break;
			}
			} catch (Exception e) {
				mav.addObject("msg","베너 등록 실패하였습니다 잠시후 다시 시도해주세요");
				mav.addObject("url","/membership/membershipForm");
				e.printStackTrace();
			}
			
			
		}
		mav.setViewName("alert");
		return mav;
	}
	
}
