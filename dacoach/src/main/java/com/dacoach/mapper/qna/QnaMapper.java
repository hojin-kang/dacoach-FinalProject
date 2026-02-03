package com.dacoach.mapper.qna;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.model.qna.QnaDTO;
import com.dacoach.model.qna.Qna_aDTO;

@Mapper
public interface QnaMapper {

	// 내 문의 목록
    public List<QnaDTO> myQnaList(int user_idx);

    // 내 문의 상세
    public QnaDTO myQnaDetail(Map<String, Object> param);
    
    // 답변 여부
    public Qna_aDTO selectAnswer(int qna_idx);
    
    // 문의 작성
    public int myQnaNew(QnaDTO qdto);
}
