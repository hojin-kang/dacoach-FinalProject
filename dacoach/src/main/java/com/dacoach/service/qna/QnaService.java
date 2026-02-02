package com.dacoach.service.qna;

import java.util.*;

import com.dacoach.model.qna.QnaDTO;

public interface QnaService {

	// 내 문의 목록
    List<QnaDTO> myQnaList(int userIdx);

    // 문의 상세
    QnaDTO myQnaDetail(int qnaIdx, int userIdx);
}
