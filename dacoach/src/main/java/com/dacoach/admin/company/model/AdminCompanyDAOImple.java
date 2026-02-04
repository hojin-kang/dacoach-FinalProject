package com.dacoach.admin.company.model;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.users.UsersDTO;

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
	public int updateUsersStatus(Map<String, Object> param) {
		  return sqlSession.update(NS + "updateUsersStatus", param);
	}

	@Override
	public int upsertSuspendSetting(Map<String, Object> param) {
		return sqlSession.update(NS + "upsertSuspendSetting", param);
	}

	@Override
	public int updateCertStatus(Map<String, Object> param) {
		return sqlSession.update(NS + "updateCertStatus", param);
	}

}

