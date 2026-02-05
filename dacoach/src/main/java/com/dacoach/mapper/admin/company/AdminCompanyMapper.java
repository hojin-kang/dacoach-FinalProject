package com.dacoach.mapper.admin.company;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dacoach.admin.company.model.AdminCompanyRowDTO;
import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.admin.EmbeddedUserDTO;

@Mapper
public interface AdminCompanyMapper {

    List<CompanyDTO> companyList(); // (네 기존 유지)

    AdminCompanyRowDTO companyDetail(@Param("usersIdx") int usersIdx);

    // CERT
    int countCertByUserAndType(@Param("userIdx") long userIdx,
                               @Param("certType") String certType);

    int updateCertStatus(@Param("user_idx") long userIdx,
                         @Param("cert_status") String certStatus);

    int insertCertStatus(@Param("user_idx") long userIdx,
                         @Param("cert_status") String certStatus);

    // USERS
    int updateUserStatus(@Param("userIdx") long userIdx,
                         @Param("status") String status);

    // EMBEDDED_USER (정지 이력 누적)
    int insertEmbeddedHistory(@Param("userIdx") long userIdx,
                              @Param("startDate") String startDate, // yyyy-MM-dd
                              @Param("endDate") String endDate,     // yyyy-MM-dd or null/""
                              @Param("reason") String reason);

    int closeLatestEmbeddedHistory(@Param("userIdx") long userIdx);

    EmbeddedUserDTO selectLatestEmbeddedByUser(@Param("userIdx") long userIdx);
}
