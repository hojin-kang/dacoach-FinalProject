package com.dacoach.service.challenge;

import java.util.HashMap;
import java.util.List;

import com.dacoach.model.challenge.ChallengeDTO;

public interface ChallengeService {
	public List<ChallengeDTO> getAllChallenges(int user_idx) throws Exception;
	public int insertDefaultChallenges(int user_idx) throws Exception;
	public HashMap<String, Object> getScores(int user_idx) throws Exception;
	public int achieveChallenge(ChallengeDTO challenge) throws Exception;
}
