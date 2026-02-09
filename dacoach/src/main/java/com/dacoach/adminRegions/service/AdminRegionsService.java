package com.dacoach.adminRegions.service;

import java.util.List;
import java.util.Map;

import com.dacoach.majorregion.model.MajorRegionDTO;
import com.dacoach.minorregion.model.MinorRegionDTO;

public interface AdminRegionsService {

	 List<MajorRegionDTO> getMajorRegions();
	 List<MinorRegionDTO> getMinorRegions(int majorIdx);
}
