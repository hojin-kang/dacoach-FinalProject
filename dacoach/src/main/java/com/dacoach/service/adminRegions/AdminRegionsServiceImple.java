package com.dacoach.service.adminRegions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.majorregion.model.MajorRegionDTO;
import com.dacoach.mapper.adminregions.AdminRegionsMapper;
import com.dacoach.minorregion.model.MinorRegionDTO;

@Service
public class AdminRegionsServiceImple implements AdminRegionsService {

    @Autowired
    private AdminRegionsMapper mapper;

	@Override
	public List<MajorRegionDTO> getMajorRegions() {
		return mapper.selectMajorRegions();
	}

	@Override
	public List<MinorRegionDTO> getMinorRegions(int majorIdx) {
		return mapper.selectMinorRegions(majorIdx);
	}

	@Override
	public int updateMinorRegions(MinorRegionDTO dto) {
		return mapper.updateMinorRegions(dto);
	}

	@Override
	public int addMinorRegion(MinorRegionDTO dto) {
		return mapper.insertMinorRegions(dto);
	}


}