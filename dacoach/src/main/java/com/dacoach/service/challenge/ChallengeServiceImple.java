package com.dacoach.service.challenge;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.model.challenge.ChallengeDTO;
import com.dacoach.mapper.challenge.*;
@Service
public class ChallengeServiceImple implements ChallengeService {
	@Autowired
	private ChallengeMapper challengeMapper;

	@Override
	public List<ChallengeDTO> getAllChallenges(int user_idx) {
		List<ChallengeDTO> lists=challengeMapper.getAllChallenges(user_idx);
		return lists;
	}

	@Override
	public int insertDefaultChallenges(int user_idx) throws Exception {
		int result=challengeMapper.insertDefaultChallenges(user_idx);
		return result;
	}

	@Override
	public HashMap<String, Object> getScores(int user_idx) throws Exception {
		HashMap<String, Object> scores=challengeMapper.getScores(user_idx);
		return scores;
	}

	@Override
	public int achieveChallenge(ChallengeDTO challenge) throws Exception {
		int result=challengeMapper.achieveChallenge(challenge);
		challengeMapper.addPoints(challenge);
		return result;
	}

}
