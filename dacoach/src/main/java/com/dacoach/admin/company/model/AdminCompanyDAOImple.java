package com.dacoach.admin.company.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminCompanyDAOImple implements AdminCompanyDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;
    
    private static final String NS = "com.dacoach.mapper.admin.company.AdminCompanyMapper.";

    @Override
    public List<Map<String, Object>> companyList() {
        return sqlSession.selectList(NS + "companyList");
    }

    @Override
    public Map<String, Object> companyDetail(long usersIdx) {
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
    public int insertEmbeddedHistory(Map<String, Object> param) {
        return sqlSession.insert(NS + "insertEmbeddedHistory", param);
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
        return sqlSession.selectOne(NS + "countCertByUserAndType", p);
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
    public int countClassTotal() {
        return sqlSession.selectOne(NS + "countClassTotal");
    }
    
    @Override
    public Map<String, Object> classDetail(int classIdx) {
        Map<String, Object> p = new HashMap<>();
        p.put("classIdx", classIdx);
        return sqlSession.selectOne(NS + "classDetail", p);
    }

    @Override
    public Map<String, Object> selectClassRatingSummary(int classIdx) {
        return sqlSession.selectOne(NS + "selectClassRatingSummary", classIdx);
    }

    @Override
    public int selectClassEnrollCount(int classIdx) {
        Integer count = sqlSession.selectOne(NS + "selectClassEnrollCount", classIdx);
        return count != null ? count : 0;
    }
   
    @Override
    public List<Map<String, Object>> selectClassReviews(int classIdx) {
        return sqlSession.selectList(NS + "selectClassReviews", classIdx);
    }

    @Override
    public int deleteReview(int reviewIdx) {
        return sqlSession.delete(NS + "deleteReview", reviewIdx);
    }
}