package com.dacoach.admin.company.model;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class AdminCompanyDAOImple implements AdminCompanyDAO {

	private final SqlSessionTemplate sqlSession;
	  private static final String NS = "com.dacoach.mapper.admin.company.AdminCompanyMapper.";

	  public AdminCompanyDAOImple(SqlSessionTemplate sqlSession) {
	    this.sqlSession = sqlSession;
	  }

	  @Override
	  public List<AdminCompanyRowDTO> companyList() {
	    return sqlSession.selectList(NS + "companyList");
	  }

	  @Override
	  public AdminCompanyRowDTO companyDetail(int usersIdx) {
	    return sqlSession.selectOne(NS + "companyDetail", usersIdx);
	  }

	  @Override
	  public int upsertCertStatus(int usersIdx, String certStatus) {
	    Map<String, Object> p = new HashMap<>();
	    p.put("usersIdx", usersIdx);
	    p.put("certType", "사업증");
	    p.put("certName", "사업자등록증"); // CERT_NAME NOT NULL 대응
	    p.put("certStatus", certStatus);    // 대기/확인
	    return sqlSession.update(NS + "upsertCertStatus", p);
	  }

	  @Override
	  public int updateUserStatus(int usersIdx, String status) {
	    Map<String, Object> p = new HashMap<>();
	    p.put("usersIdx", usersIdx);
	    p.put("status", status);
	    return sqlSession.update(NS + "updateUserStatus", p);
	  }

	  @Override
	  public int upsertEmbeddedUser(int usersIdx, Date startDate, Date endDate, String reason) {
	    Map<String, Object> p = new HashMap<>();
	    p.put("usersIdx", usersIdx);
	    p.put("startDate", startDate);
	    p.put("endDate", endDate);
	    p.put("reason", reason);
	    return sqlSession.update(NS + "upsertEmbeddedUser", p);
	  }

}
