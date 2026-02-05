// ================================
// 1) AdminCompanyDAOImple (SqlSessionTemplate만 사용)
// ================================
package com.dacoach.admin.company.model;

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
        // mapper xml에서 #{usersIdx} 쓰면 안전하게 map으로 전달
        Map<String, Object> p = new HashMap<>();
        p.put("usersIdx", usersIdx);
        return sqlSession.selectOne(NS + "companyDetail", p);
    }

    @Override
    public int updateUserStatus(long userIdx, String status) {
        Map<String, Object> p = new HashMap<>();
        p.put("userIdx", userIdx);
        p.put("status", status);
        return sqlSession.update(NS + "updateUserStatus", p);
    }

    @Override
    public int insertEmbeddedHistory(long userIdx, String startDate, String endDate, String reason) {
        Map<String, Object> p = new HashMap<>();
        p.put("userIdx", userIdx);
        p.put("startDate", startDate); // yyyy-MM-dd
        p.put("endDate", endDate);     // yyyy-MM-dd or null/""
        p.put("reason", reason);
        return sqlSession.insert(NS + "insertEmbeddedHistory", p);
    }

    @Override
    public int closeLatestEmbeddedHistory(long userIdx) {
        Map<String, Object> p = new HashMap<>();
        p.put("userIdx", userIdx);
        return sqlSession.update(NS + "closeLatestEmbeddedHistory", p);
    }

    @Override
    public int countCertByUserAndType(long userIdx, String certType) {
        Map<String, Object> p = new HashMap<>();
        p.put("userIdx", userIdx);
        p.put("certType", certType);
        Integer n = sqlSession.selectOne(NS + "countCertByUserAndType", p);
        return (n == null) ? 0 : n;
    }

    @Override
    public int updateCertStatus(long userIdx, String certStatus) {
        // 너가 준 xml 파라미터명(user_idx, cert_status) 그대로 맞춤
        Map<String, Object> p = new HashMap<>();
        p.put("user_idx", userIdx);
        p.put("cert_status", certStatus);
        return sqlSession.update(NS + "updateCertStatus", p);
    }

    @Override
    public int insertCertStatus(long userIdx, String certStatus) {
        Map<String, Object> p = new HashMap<>();
        p.put("user_idx", userIdx);
        p.put("cert_status", certStatus);
        return sqlSession.insert(NS + "insertCertStatus", p);
    }
}
