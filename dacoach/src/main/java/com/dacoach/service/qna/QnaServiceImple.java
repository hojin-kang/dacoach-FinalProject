package com.dacoach.service.qna;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.qna.QnaMapper;
import com.dacoach.model.qna.QnaDTO;
import com.dacoach.model.qna.Qna_aDTO;

@Service
public class QnaServiceImple implements QnaService {

	@Autowired
    private QnaMapper qnaMapper;

	
	@Override
	public List<QnaDTO> myQnaList(int user_idx) {
		return qnaMapper.myQnaList(user_idx);
	}

	@Override
	public QnaDTO myQnaDetail(int qna_idx, int user_idx) {
		Map<String, Object> param = new HashMap<>();
        param.put("qnaIdx", qna_idx);
        param.put("userIdx", user_idx);

        return qnaMapper.myQnaDetail(param);
	}
	
	@Override
	public Map<String, Object> myQnaDetailWithAnswer(int qna_idx, int user_idx) {
		Map<String, Object> param = new HashMap<>();
	    param.put("qnaIdx", qna_idx);
	    param.put("userIdx", user_idx);

	    QnaDTO qna = qnaMapper.myQnaDetail(param);
	    Qna_aDTO answer = qnaMapper.selectAnswer(qna_idx);

	    Map<String, Object> result = new HashMap<>();
	    result.put("qna", qna);
	    result.put("answer", answer);
	    return result;
	}
	
	@Override
	public int myQnaNew(QnaDTO qdto) {
		int result = qnaMapper.myQnaNew(qdto);
		return result;
	}

}
