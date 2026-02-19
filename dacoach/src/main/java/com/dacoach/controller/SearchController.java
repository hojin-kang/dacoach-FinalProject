package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.coachClasses.CoachClassDTO;
import com.dacoach.service.coachClasses.CoachClassService;
import com.dacoach.service.coachSearch.CoachSearchService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SearchController {

	@Autowired
	private CoachSearchService coachSearchService;

	@Autowired
	private CoachClassService classService;

	@GetMapping("/searchResult")
	public String searchResult(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
			HttpSession session, Model model) throws Exception {

		String kw = keyword == null ? "" : keyword.trim();

		// ===== 코치(4개 미리보기) =====
		int loginIdx = session.getAttribute("user_idx") == null ? 0 : (int) session.getAttribute("user_idx");

		HashMap<String, Object> coachParam = new HashMap<>();
		coachParam.put("keyword", kw);
		coachParam.put("sort", "latest");
		coachParam.put("majorField", 0);
		coachParam.put("minorField", 0);
		coachParam.put("majorRegion", 0);
		coachParam.put("minorRegion", 0);
		coachParam.put("login_idx", loginIdx);

		List<CoachDTO> coachList = coachSearchService.coachList(1, coachParam);
		int coachCnt = (coachList != null && !coachList.isEmpty()) ? coachList.get(0).getTotal_cnt() : 0;

		// ===== 클래스(4개 미리보기) =====
		List<CoachClassDTO> classList = classService.classSearchPaged(1, 6, null, null, null, null, kw, "latest");

		int classCnt = (classList != null && !classList.isEmpty()) ? classList.get(0).getTotal_cnt() : 0;

		// ===== model =====
		model.addAttribute("keyword", kw);

		model.addAttribute("coachList", coachList);
		model.addAttribute("coachCnt", coachCnt);

		model.addAttribute("classList", classList);
		model.addAttribute("classCnt", classCnt);

		return "searchResult";
	}
}
