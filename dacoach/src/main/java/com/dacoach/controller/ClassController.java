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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.dacoach.mapper.classes.ClassMapper;
import com.dacoach.model.classes.ClassDTO;
import com.dacoach.service.classes.ClassService;

@Controller
@RequestMapping("/class")
public class ClassController {

	@Autowired
	private ClassService classService;

	@Autowired
	private ClassMapper classMapper;

	/**
	 * 클래스 등록 폼 페이지
	 */
	@GetMapping("/register")
	public String classRegisterForm(Model model) {
		model.addAttribute("classDTO", new ClassDTO());
		return "company/classes/classRegister";
	}

	/**
	 * 대분류 조회 (Ajax)
	 */
	@GetMapping("/majorFields")
	@ResponseBody
	public List<Map<String, Object>> getMajorFields() {
		return classMapper.selectMajorFields();
	}

	/**
	 * 소분류 조회 (Ajax - 대분류 선택 시)
	 */
	@GetMapping("/minorFields")
	@ResponseBody
	public List<Map<String, Object>> getMinorFields(@RequestParam Integer majorFieldIdx) {
		return classMapper.selectMinorFields(majorFieldIdx);
	}

	/**
	 * 대지역 조회 (Ajax)
	 */
	@GetMapping("/majorRegions")
	@ResponseBody
	public List<Map<String, Object>> getMajorRegions() {
		return classMapper.selectMajorRegions();
	}

	/**
	 * 소지역 조회 (Ajax - 대지역 선택 시)
	 */
	@GetMapping("/minorRegions")
	@ResponseBody
	public List<Map<String, Object>> getMinorRegions(@RequestParam Integer majorRegionIdx) {
		return classMapper.selectMinorRegions(majorRegionIdx);
	}

	/**
	 * 클래스 등록 처리
	 */
	@PostMapping("/register")
	public String classRegisterSubmit(@ModelAttribute ClassDTO classDTO, HttpSession session, RedirectAttributes rttr) {
		try {
			// 세션에서 사용자 정보 가져오기
			Integer usersIdx = (Integer) session.getAttribute("users_idx");

			if (usersIdx == null) {
				rttr.addFlashAttribute("error", "로그인이 필요합니다.");
				return "redirect:/login";
			}

			classDTO.setProvider_idx(usersIdx);

			// 백엔드 유효성 검사
			String validationError = validateClass(classDTO);
			if (validationError != null) {
				rttr.addFlashAttribute("error", validationError);
				return "redirect:/class/register";
			}

			// 클래스 등록
			int result = classService.classRegister(classDTO);

			if (result > 0) {
				rttr.addFlashAttribute("msg", "클래스 등록이 완료되었습니다!");
				return "redirect:/class/list";
			} else {
				rttr.addFlashAttribute("error", "클래스 등록에 실패했습니다.");
			}
		} catch (Exception e) {
			rttr.addFlashAttribute("error", "오류 발생: " + e.getMessage());
			e.printStackTrace();
		}

		return "redirect:/class/register";
	}

	/**
	 * 클래스 유효성 검사
	 */
	private String validateClass(ClassDTO classDTO) {
		// 필수 입력 확인
		if (classDTO.getTitle() == null || classDTO.getTitle().trim().isEmpty()) {
			return "제목을 입력해주세요.";
		}
		if (classDTO.getIntro() == null || classDTO.getIntro().trim().isEmpty()) {
			return "내용을 입력해주세요.";
		}
		if (classDTO.getMinor_field_idx() == null) {
			return "클래스 분야를 선택해주세요.";
		}
		if (classDTO.getMinor_region_idx() == null) {
			return "클래스 장소를 선택해주세요.";
		}
		if (classDTO.getPrice() == null || classDTO.getPrice() < 0) {
			return "올바른 가격을 입력해주세요.";
		}
		if (classDTO.getStart_date() == null) {
			return "시작일을 입력해주세요.";
		}
		if (classDTO.getEnd_date() == null) {
			return "종료일을 입력해주세요.";
		}
		if (classDTO.getMax_user_cnt() == null || classDTO.getMax_user_cnt() <= 0) {
			return "수강 인원을 올바르게 입력해주세요.";
		}

		// 날짜 검증
		Date now = new Date();
		if (classDTO.getStart_date().before(now)) {
			return "시작일은 현재 시간 이후여야 합니다.";
		}
		if (classDTO.getEnd_date().before(classDTO.getStart_date())
				|| classDTO.getEnd_date().equals(classDTO.getStart_date())) {
			return "종료일은 시작일보다 이후여야 합니다.";
		}

		// 제목/내용 길이 검증
		if (classDTO.getTitle().length() > 200) {
			return "제목은 200자 이내로 입력해주세요.";
		}
		if (classDTO.getIntro().length() > 3000) {
			return "내용은 3000자 이내로 입력해주세요.";
		}

		return null; // 유효성 검사 통과
	}

	/**
	 * 클래스 목록 조회
	 */
	@GetMapping("/list")
	public String classList(Model model) {
		// TODO: 클래스 목록 조회 로직 구현 예정
		return "company/classes/classList";
	}
	
	/************* coach *************/
	@GetMapping("/coach/classList")
	public ModelAndView coachClassList() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/coach/classes/classList");
		return mav;
	}
	/*********************************/
}