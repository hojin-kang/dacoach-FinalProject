package com.dacoach.mapper.adminregions;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dacoach.majorregion.model.MajorRegionDTO;
import com.dacoach.minorregion.model.MinorRegionDTO;

@Mapper
public interface AdminRegionsMapper {
    
    // 대분류 목록 조회
    List<MajorRegionDTO> selectMajorRegions();
    
    // 소분류 목록 조회 (대분류별)
    List<MinorRegionDTO> selectMinorRegions(@Param("majorRegionIdx") int majorIdx);
    
    // 소분류 수정
    int updateMinorRegions(MinorRegionDTO dto);
    
    // 소분류 추가
    int insertMinorRegions(MinorRegionDTO dto);
}