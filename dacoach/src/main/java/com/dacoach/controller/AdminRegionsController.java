package com.dacoach.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dacoach.adminRegions.service.AdminRegionsService;
import com.dacoach.majorregion.model.MajorRegionDTO;
import com.dacoach.minorregion.model.MinorRegionDTO;

@Controller
@RequestMapping("/admin/filter")
public class AdminRegionsController {

	@Autowired
	private AdminRegionsService service;
	
	@GetMapping("/regions")
	public String getMajorRegions(Model model) {
	    List<MajorRegionDTO> majorRegions = service.getMajorRegions();

	    model.addAttribute("majorRegions", majorRegions);
	    model.addAttribute("contentPage", "admin/filter/regionalFilters");
	    model.addAttribute("contentFragment", "contentPage");
	    return "admin/dashboard";
	}
	
	@GetMapping("/major")
	public String getMajorRegions(@RequestParam(value="majorIdx", required=false) Integer majorIdx, Model model) {
	    List<MajorRegionDTO> majorRegions = service.getMajorRegions();
	    model.addAttribute("majorRegions", majorRegions);

	    List<MinorRegionDTO> subRegions = null;
	    if (majorIdx != null) {
	        subRegions = service.getMinorRegions(majorIdx);
	        model.addAttribute("selectedMajorIdx", majorIdx);
	    }
	    
	    model.addAttribute("subRegions", subRegions); 

	    model.addAttribute("contentPage", "admin/filter/regionalFilters");
	    model.addAttribute("contentFragment", "contentPage");
	    return "admin/dashboard";
	}
	
	
}
