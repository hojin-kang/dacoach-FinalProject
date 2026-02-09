package com.dacoach.admin.regions.model;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dacoach.majorregion.model.MajorRegionDTO;
import com.dacoach.minorregion.model.MinorRegionDTO;

@Repository
public class AdminRegionsDAOImple implements AdminRegionsDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	private static final String Re = "com.dacoach.mapper.adminregions.AdminRegionsMapper.";
	@Override
	public List<MajorRegionDTO> selectMajorRegions() {
		return sqlSession.selectList(Re+"selectMajorRegions");
	}
	@Override
	public List<MinorRegionDTO> selectMinorRegions(int majorIdx) {
		return sqlSession.selectList(Re+"selectMinorRegions", majorIdx);
	}

}
