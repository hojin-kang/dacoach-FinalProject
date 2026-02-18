package com.dacoach.service.company;

import java.util.List;
import java.util.Map;

import com.dacoach.model.chat.ChatMessageDTO;
import com.dacoach.model.chat.ChatRoomDTO;
import com.dacoach.model.classes.ClassDTO;
import com.dacoach.model.coachClasses.ClassEnrollmentDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.company.CompanyProvideDTO;
import com.dacoach.model.company.CompanyRegionDTO;
import com.dacoach.model.users.UsersDTO;

public interface CompanyService {
	
	public List<Map<String, Object>> fieldTeg() throws Exception;
	public List<Map<String, Object>> regionTeg() throws Exception;
	public List<Map<String, Object>> getRegionTag(int idx) throws Exception;
	public int joinOk(UsersDTO dto) throws Exception;
	public int getUserIdx(String login_id) throws Exception;
	public int companyInfo(CompanyDTO dto) throws Exception;
	public int insertcert(CertDTO dto) throws Exception;
	public int getCompanyNum(int idx) throws Exception;
	public int addRegion(CompanyRegionDTO dto) throws Exception;
	public List<Map<String,Object>> getMinorField(int idx) throws Exception;
	public int provideOk(CompanyProvideDTO dto) throws Exception;
	public CompanyDTO getCompanyInfo(int idx) throws Exception;
	public boolean regionCheck(int idx) throws Exception;
	public boolean provideCheck(int idx)	throws Exception;
	public List<Map<String,Object>> getCompanyRegion(int idx) throws Exception;
	public Map<String,Object> companyProfile(int member_idx) throws Exception;
	public List<Map<String,Object>> getProfileClass(Map<String,Object> map)throws Exception;
	public CertDTO getCompanyCert(int user_idx) throws Exception;
	public Map<String,Object> getCompanyProvide(int company_idx) throws Exception;
	public List<CompanyRegionDTO> getCompanyAllRegion(int company_idx) throws Exception;
	public boolean companyUp(CompanyDTO companyDto,CompanyProvideDTO provideDto) throws Exception;
	public int regionDel(CompanyRegionDTO dto) throws Exception;
	public List<Map<String,Object>> getPayHistory(int user_idx) throws Exception;
	public List<Map<String,Object>> getCoachList() throws Exception;
	public List<ClassDTO> getClass(int user_idx) throws Exception;
	public List<ClassEnrollmentDTO> getUserClass(int class_idx) throws Exception;
	public ChatRoomDTO getClassRoom(int roomIdx) throws Exception;
	public ChatMessageDTO saveAndBuildBroadcast(ChatMessageDTO msg);
	public String loadMessagesRawCustom(int roomIdx, int myIdx);
}
