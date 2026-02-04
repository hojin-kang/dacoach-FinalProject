package com.dacoach.controller;

import jakarta.servlet.http.HttpSession;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

	@GetMapping("/company/register")
	public ModelAndView classRegisterForm() {
		ModelAndView mav = new ModelAndView("company/classes/classRegister");
		mav.addObject("classDTO", new ClassDTO());
		return mav;
	}

	@GetMapping("/majorFields")
	@ResponseBody
	public List<Map<String, Object>> getMajorFields() throws Exception {
		return classService.getMajorFields();
	}

	@GetMapping("/minorFields")
	@ResponseBody
	public List<Map<String, Object>> getMinorFields(@RequestParam Integer majorFieldIdx) throws Exception {
		return classService.getMinorFields(majorFieldIdx);
	}

	@GetMapping("/majorRegions")
	@ResponseBody
	public List<Map<String, Object>> getMajorRegions() throws Exception {
		return classService.getMajorRegions();
	}

	@GetMapping("/minorRegions")
	@ResponseBody
	public List<Map<String, Object>> getMinorRegions(@RequestParam Integer majorRegionIdx) throws Exception {
		return classService.getMinorRegions(majorRegionIdx);
	}

	@PostMapping("/company/register")
	public ModelAndView classRegisterSubmit(@ModelAttribute ClassDTO classDTO,
			@RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
			@RequestParam(value = "videoFile", required = false) MultipartFile videoFile, HttpSession session,
			RedirectAttributes rttr) {

		ModelAndView mav = new ModelAndView("redirect:/class/company/classList");

		try {
			Integer userIdx = (Integer) session.getAttribute("user_idx");
			if (userIdx == null) {
				rttr.addFlashAttribute("error", "로그인이 필요합니다.");
				mav.setViewName("redirect:/login");
				return mav;
			}

			classDTO.setProvider_idx(userIdx);

			if (photoFile != null && !photoFile.isEmpty()) {
				classDTO.setPhotoFile(photoFile);
			}
			if (videoFile != null && !videoFile.isEmpty()) {
				classDTO.setVideoFile(videoFile);
			}

			int result = classService.classRegister(classDTO);

			if (result > 0) {
				rttr.addFlashAttribute("msg", "클래스 등록이 완료되었습니다!");
			} else {
				rttr.addFlashAttribute("error", "클래스 등록에 실패했습니다.");
				mav.setViewName("redirect:/class/company/register");
			}
		} catch (Exception e) {
			rttr.addFlashAttribute("error", "오류 발생: " + e.getMessage());
			mav.setViewName("redirect:/class/company/register");
			e.printStackTrace();
		}

		return mav;
	}

	/************* company 
	 * @throws Exception *************/
	@GetMapping("/company/classList")
	public ModelAndView classList(HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView("company/classes/classList");

		Integer providerIdx = (Integer) session.getAttribute("user_idx");
		if (providerIdx == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}

		List<ClassDTO> classList = classService.getClassesByProvider(providerIdx);
		mav.addObject("classList", classList);
		return mav;
	}

	/*********************************/

	/************* coach 
	 * @throws Exception *************/
	// 코치 - 클래스 검색
	@GetMapping("/coach/classList")
	public ModelAndView coachClassList(
	        @RequestParam(required = false) Integer majorField,
	        @RequestParam(required = false) Integer minorField,
	        @RequestParam(required = false) Integer majorRegion,
	        @RequestParam(required = false) Integer minorRegion,
	        @RequestParam(required = false) String q,
	        @RequestParam(required = false, defaultValue = "latest") String sort
	) throws Exception {
	    ModelAndView mav = new ModelAndView();

	    List<Map<String, Object>> majorList = classService.getMajorFields();
	    List<Map<String, Object>> majorRegions = classService.getMajorRegions();

	    List<ClassDTO> classList = classService.searchCoachClasses(minorField, minorRegion, q, sort);

	    mav.addObject("majorList", majorList);
	    mav.addObject("majorRegions", majorRegions);
	    mav.addObject("classList", classList);

	    // 검색값 유지
	    mav.addObject("majorField", majorField);
	    mav.addObject("minorField", minorField);
	    mav.addObject("majorRegion", majorRegion);
	    mav.addObject("minorRegion", minorRegion);
	    mav.addObject("q", q);
	    mav.addObject("sort", sort);

	    mav.setViewName("coach/classes/classList");
	    return mav;
	}

	/*********************************/
}