package com.dacoach.adminRegions.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.admin.regions.model.AdminRegionsDAO;
import com.dacoach.majorregion.model.MajorRegionDTO;
import com.dacoach.minorregion.model.MinorRegionDTO;

@Service
public class AdminRegionsServiceImple implements AdminRegionsService {

	@Autowired
	private AdminRegionsDAO dao;

	@Override
	public List<MajorRegionDTO> getMajorRegions() {
		return dao.selectMajorRegions();
	}

	@Override
	public List<MinorRegionDTO> getMinorRegions(int majorIdx) {
		return dao.selectMinorRegions(majorIdx);
	}

	@Override
	public void saveMinorRegion(MinorRegionDTO dto) {
		if(dto.getMinor_region_idx() > 0) {
			dao.updateMinorRegions(dto);
		} else {
			
		}
	}

	
	

}
