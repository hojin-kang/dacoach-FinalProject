package com.dacoach.controller;

import jakarta.servlet.http.HttpSession;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.dacoach.model.classes.ClassDTO;
import com.dacoach.service.classes.ClassService;

@Controller
@RequestMapping("/class")
public class ClassController {

	@Autowired
	private ClassService classService;

	@GetMapping("/register")
	public String classRegisterForm(Model model) {
		model.addAttribute("classDTO", new ClassDTO());
		return "company/classes/classRegister";
	}

	@GetMapping("/majorFields")
	@ResponseBody
	public List<Map<String, Object>> getMajorFields() {
		return classService.getMajorFields();
	}

	@GetMapping("/minorFields")
	@ResponseBody
	public List<Map<String, Object>> getMinorFields(@RequestParam Integer majorFieldIdx) {
		return classService.getMinorFields(majorFieldIdx);
	}

	@GetMapping("/majorRegions")
	@ResponseBody
	public List<Map<String, Object>> getMajorRegions() {
		return classService.getMajorRegions();
	}

	@GetMapping("/minorRegions")
	@ResponseBody
	public List<Map<String, Object>> getMinorRegions(@RequestParam Integer majorRegionIdx) {
		return classService.getMinorRegions(majorRegionIdx);
	}

	@PostMapping("/register")
	public String classRegisterSubmit(
			@ModelAttribute ClassDTO classDTO,
			@RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
			@RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
			HttpSession session,
			RedirectAttributes rttr) {
		try {
			// 세션에서 사용자 정보 가져오기
			Integer userIdx = (Integer) session.getAttribute("user_idx");

			if (userIdx == null) {
				rttr.addFlashAttribute("error", "로그인이 필요합니다.");
				return "redirect:/login";
			}

			classDTO.setProvider_idx(userIdx);

			// 파일 설정
			if (photoFile != null && !photoFile.isEmpty()) {
				classDTO.setPhotoFile(photoFile);
			}
			
			if (videoFile != null && !videoFile.isEmpty()) {
				classDTO.setVideoFile(videoFile);
			}

			// Service를 통해 클래스 등록
			int result = classService.classRegister(classDTO);

			if (result > 0) {
				rttr.addFlashAttribute("msg", "클래스 등록이 완료되었습니다!");
				return "redirect:/class/company/classList";
			} else {
				rttr.addFlashAttribute("error", "클래스 등록에 실패했습니다.");
			}
		} catch (Exception e) {
			rttr.addFlashAttribute("error", "오류 발생: " + e.getMessage());
			e.printStackTrace();
		}

		return "redirect:/class/register";
	}

	/************* company *************/
	@GetMapping("/company/classList")
	public String classList(Model model, HttpSession session) {
	    // 세션에서 provider_idx 가져오기
	    Integer providerIdx = (Integer) session.getAttribute("user_idx");
	    
	    if (providerIdx == null) {
	        // 로그인 안된 경우 처리
	        model.addAttribute("error", "로그인이 필요합니다.");
	        return "redirect:/login";
	    }
	    
	    // 클래스 목록 조회
	    List<ClassDTO> classList = classService.getClassesByProvider(providerIdx);
	    model.addAttribute("classList", classList);
	    
	    return "company/classes/ongoingClassList";  // HTML 템플릿 경로
	}
	/*********************************/
	
	/************* coach *************/
	@GetMapping("/coach/classList")
	public ModelAndView coachClassList() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/coach/classes/classList");
		return mav;
	}
	/*********************************/
}