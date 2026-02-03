package com.dacoach.service.qna;

import java.util.*;

import com.dacoach.model.qna.QnaDTO;

public interface QnaService {

	// 내 문의 목록
    public List<QnaDTO> myQnaList(int user_idx);

    // 문의 상세
    public QnaDTO myQnaDetail(int qna_idx, int user_idx);
    
    // 답변
    public Map<String, Object> myQnaDetailWithAnswer(int qna_idx, int user_idx);
    
    // 문의 작성
    public int myQnaNew(QnaDTO qdto);
}
