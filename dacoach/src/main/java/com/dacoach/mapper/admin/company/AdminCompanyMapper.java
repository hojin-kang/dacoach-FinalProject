package com.dacoach.mapper.admin.company;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dacoach.admin.company.model.AdminCompanyRowDTO;
import com.dacoach.model.company.CompanyDTO;

@Mapper
public interface AdminCompanyMapper {
    List<CompanyDTO> companyList();
    AdminCompanyRowDTO companyDetail(int usersIdx);
    
    int upsertCertStatus(Map<String, Object> param);
    
    int upsertEmbeddedUser(@Param("usersIdx") int usersIdx,
            @Param("suspendUntil") java.sql.Date suspendUntil,
            @Param("reason") String reason);

}
