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
    public List<Map<String, Object>> companyList() {
        return sqlSession.selectList(NS + "companyList");
    }

    @Override
    public Map<String, Object> companyDetail(int usersIdx) {
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
        p.put("startDate", startDate);
        p.put("endDate", endDate);
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

    @Override
    public List<Map<String, Object>> selectClassPage(Map<String, Object> param) {
        return sqlSession.selectList(NS + "selectClassPage", param);
    }

    @Override
    public List<Map<String, Object>> selectReviewSummaryByClassIds(List<Integer> classIds) {
        return sqlSession.selectList(NS + "selectReviewSummaryByClassIds", classIds);
    }

    @Override
    public int countClassTotal() {
        return sqlSession.selectOne(NS + "countClassTotal");
    }
}