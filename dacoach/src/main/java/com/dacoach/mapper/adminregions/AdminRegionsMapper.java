package com.dacoach.mapper.adminregions;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.majorregion.model.MajorRegionDTO;
import com.dacoach.minorregion.model.MinorRegionDTO;

@Mapper
public interface AdminRegionsMapper {
	List<MajorRegionDTO> selectMajorRegions();
	List<MinorRegionDTO> selectMinorRegions(int majorIdx);
	int updateMinorRegions (MajorRegionDTO majorRegion);
}