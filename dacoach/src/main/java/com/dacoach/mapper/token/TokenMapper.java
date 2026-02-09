package com.dacoach.mapper.token;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dacoach.model.token.TokenHistoryDTO;

@Mapper
public interface TokenMapper {
    Integer getMyToken(@Param("user_idx") Integer user_idx);
    List<TokenHistoryDTO> getTokenHistory(@Param("user_idx") Integer user_idx);

    int updateTokenBalance(@Param("user_idx") Integer user_idx,
                           @Param("token_balance") Integer token_balance);

    int insertTokenHistory(Map<String, Object> map);
    
    List<TokenHistoryDTO> getChargeHistory(@Param("user_idx") Integer user_idx);
}
