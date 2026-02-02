package com.dacoach.mapper.qna;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.model.qna.QnaDTO;

@Mapper
public interface QnaMapper {

	// 내 문의 목록
    List<QnaDTO> myQnaList(int userIdx);

    // 내 문의 상세
    QnaDTO myQnaDetail(Map<String, Object> param);
}
