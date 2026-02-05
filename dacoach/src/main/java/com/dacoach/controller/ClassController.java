package com.dacoach.controller;

import jakarta.servlet.http.HttpSession;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.dacoach.service.file.FileUpload;
import com.dacoach.mapper.classes.ClassMapper;

@Controller
@RequestMapping("/class")
public class ClassController {

	@Autowired
	private ClassService classService;

	@Autowired
	private ClassMapper classMapper;

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
			@RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
			@RequestParam(value = "hashtags", required = false) String hashtags, // ⭐ 추가
			HttpSession session, RedirectAttributes rttr) {

		ModelAndView mav = new ModelAndView("redirect:/class/company/classList");

		try {
			Integer userIdx = (Integer) session.getAttribute("user_idx");
			if (userIdx == null) {
				rttr.addFlashAttribute("error", "로그인이 필요합니다.");
				mav.setViewName("redirect:/login");
				return mav;
			}

			classDTO.setProvider_idx(userIdx);

			// 파일 저장 처리 (재인 형님꺼 사용하기~)
			if (photoFile != null && !photoFile.isEmpty()) {
				String photoPath = FileUpload.saveFile(photoFile, "classes/photos");
				classDTO.setPhoto(photoPath); // "classes/photos/uuid.jpg"
			}

			if (videoFile != null && !videoFile.isEmpty()) {
				String videoPath = FileUpload.saveFile(videoFile, "classes/videos");
				classDTO.setVideo(videoPath); // "classes/videos/uuid.mp4"
			}

			// ⭐ 클래스 등록 (해시태그 포함)
			int result = classService.classRegister(classDTO, hashtags);

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

	/************* company *************/
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

	@GetMapping("/company/classDetail")
	public ModelAndView companyClassDetail(@RequestParam("id") int id) throws Exception {
		ModelAndView mav = new ModelAndView();
		ClassDTO classDTO = classService.getClassDetail(id);

		if (classDTO == null) {
			mav.setViewName("redirect:/class/coach/classList");
			return mav;
		}

		mav.addObject("classDTO", classDTO);

		// ⭐ 해시태그 목록 조회
		List<String> hashtagList = classMapper.selectHashtagsByClass(id);
		mav.addObject("hashtagList", hashtagList);

		// ⭐ 분야 정보 조회
		Map<String, Object> fieldInfo = classMapper.selectClassFieldInfo(id);
		mav.addObject("fieldInfo", fieldInfo);

		// 일단 화면 안 터지게 최소값도 같이
		mav.addObject("providerName", "");
		mav.addObject("providerPhoto", "");
		mav.addObject("avgRating", 0);
		mav.addObject("reviewCount", 0);
		mav.addObject("reviewList", Collections.emptyList());

		mav.setViewName("company/classes/classDetail");
		return mav;
	}

	// 클래스 통계 페이지
	@GetMapping("/company/status")
	public String classStatus() {
		return "company/classes/classStatus";
	}

	// 클래스 목록 API (통계 페이지의 드롭다운용)
	@GetMapping("/company/classListForStats")
	@ResponseBody
	public ResponseEntity<?> getClassListForStats(HttpSession session) {
		try {
			Integer providerIdx = (Integer) session.getAttribute("user_idx");
			if (providerIdx == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
			}

			List<ClassDTO> classList = classService.getClassesByProvider(providerIdx);
			return ResponseEntity.ok(classList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("클래스 목록을 불러오는데 실패했습니다.");
		}
	}

	// 클래스 통계 API (Ajax용)
	@GetMapping("/company/status/api")
	@ResponseBody
	public ResponseEntity<?> getClassStats(@RequestParam("classIdx") int classIdx, HttpSession session) {
		try {
			Integer providerIdx = (Integer) session.getAttribute("user_idx");
			if (providerIdx == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
			}

			Map<String, Object> stats = classService.getClassStats(classIdx, providerIdx);
			return ResponseEntity.ok(stats);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("통계 데이터를 불러오는데 실패했습니다.");
		}
	}
	/*********************************/

}