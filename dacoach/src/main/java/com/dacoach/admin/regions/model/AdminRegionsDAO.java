package com.dacoach.admin.regions.model;

import java.util.List;
import java.util.Map;

import com.dacoach.majorregion.model.MajorRegionDTO;
import com.dacoach.minorregion.model.MinorRegionDTO;

public interface AdminRegionsDAO {

	List<MajorRegionDTO> selectMajorRegions();
	List<MinorRegionDTO> selectMinorRegions(int majorIdx);
	int updateMinorRegions (MinorRegionDTO dto);

}
