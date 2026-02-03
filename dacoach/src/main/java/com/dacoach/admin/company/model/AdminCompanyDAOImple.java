package com.dacoach.admin.company.model;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.users.UsersDTO;

@Repository
public class AdminCompanyDAOImple implements AdminCompanyDAO {

    private final SqlSessionTemplate sqlSession;

    public AdminCompanyDAOImple(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<AdminCompanyRowDTO> companyList() {
        return sqlSession.selectList("com.dacoach.mapper.admin.company.AdminCompanyMapper.companyList");
    }

	@Override
	public CompanyDTO companyDetail(int usersIdx) {
		return sqlSession.selectOne("com.dacoach.mapper.admin.company.AdminCompanyMapper.companyDetail",usersIdx);
	}
}

