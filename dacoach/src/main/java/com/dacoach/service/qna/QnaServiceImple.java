package com.dacoach.service.qna;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.qna.QnaMapper;
import com.dacoach.model.qna.QnaDTO;

@Service
public class QnaServiceImple implements QnaService {

	@Autowired
    private QnaMapper qnaMapper;

	
	@Override
	public List<QnaDTO> myQnaList(int userIdx) {
		return qnaMapper.myQnaList(userIdx);
	}

	@Override
	public QnaDTO myQnaDetail(int qnaIdx, int userIdx) {
		Map<String, Object> param = new HashMap<>();
        param.put("qnaIdx", qnaIdx);
        param.put("userIdx", userIdx);

        return qnaMapper.myQnaDetail(param);
	}

}
