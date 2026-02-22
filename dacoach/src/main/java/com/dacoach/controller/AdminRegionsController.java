package com.dacoach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dacoach.majorregion.model.MajorRegionDTO;
import com.dacoach.minorregion.model.MinorRegionDTO;
import com.dacoach.service.adminRegions.AdminRegionsService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/filter")
public class AdminRegionsController {

    @Autowired
    private AdminRegionsService service;

    // 지역 필터 관리 페이지 (초기 로드)
    @GetMapping("/regions")
    public String regionFilter(Model model,HttpSession session) {
    	if(session.getAttribute("loginAdmin") == null) {
    	    return "redirect:/admin";
    	}
        List<MajorRegionDTO> majorRegions = service.getMajorRegions();
        model.addAttribute("majorRegions", majorRegions);
        
        model.addAttribute("contentPage", "admin/filter/regionalFilters");
        model.addAttribute("contentFragment", "contentPage");
        return "admin/dashboard";
    }

    // 대분류 선택 시 (소분류 포함)
    @GetMapping("/major")
    public String getMajorWithMinor(
            @RequestParam(value = "majorIdx", required = false) Integer majorIdx,
            Model model
    ) {
        // 대분류 목록
        List<MajorRegionDTO> majorRegions = service.getMajorRegions();
        model.addAttribute("majorRegions", majorRegions);
        
        // 소분류 목록 (대분류 선택 시)
        if (majorIdx != null) {
            List<MinorRegionDTO> subRegions = service.getMinorRegions(majorIdx);
            model.addAttribute("subRegions", subRegions);
            model.addAttribute("selectedMajorIdx", majorIdx);
        }
        
        model.addAttribute("contentPage", "admin/filter/regionalFilters");
        model.addAttribute("contentFragment", "contentPage");
        return "admin/dashboard";
    }

    // 소분류 저장 (추가/수정)
    @PostMapping("/regions/sub/save")
    public String saveSubRegion(
            @RequestParam("major_region_idx") int majorRegionIdx,
            @RequestParam(value = "minor_region_idx", required = false) Integer minorRegionIdx,
            @RequestParam("district_nm") String districtNm
    ) {
        MinorRegionDTO dto = new MinorRegionDTO();
        dto.setMajor_region_idx(majorRegionIdx);
        dto.setDistrict_nm(districtNm);
        
        if (minorRegionIdx != null && minorRegionIdx > 0) {
            // 수정
            dto.setMinor_region_idx(minorRegionIdx);
            service.updateMinorRegions(dto);
        } else {
            // 추가
            service.addMinorRegion(dto);
        }
        
        return "redirect:/admin/filter/major?majorIdx=" + majorRegionIdx;
    }
}