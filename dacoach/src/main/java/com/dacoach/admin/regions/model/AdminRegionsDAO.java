package com.dacoach.admin.regions.model;

import java.util.List;
import java.util.Map;

public interface AdminRegionsDAO {

	List<Map<String, Object>> selectMajorRegions();
}
