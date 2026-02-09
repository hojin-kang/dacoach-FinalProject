package com.dacoach.mapper.adminregions;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminRegionsMapper {

	List<Map<String, Object>> selectMajorRegions();
}
