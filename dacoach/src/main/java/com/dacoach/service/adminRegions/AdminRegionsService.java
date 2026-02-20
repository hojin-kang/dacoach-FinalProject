package com.dacoach.service.adminRegions;

import java.util.List;

import com.dacoach.majorregion.model.MajorRegionDTO;
import com.dacoach.minorregion.model.MinorRegionDTO;

public interface AdminRegionsService {
    
    //대분류
    List<MajorRegionDTO> getMajorRegions();
    
    //소분류
    List<MinorRegionDTO> getMinorRegions(int majorIdx);
    int addMinorRegion(MinorRegionDTO dto);
    int updateMinorRegions(MinorRegionDTO dto);

}