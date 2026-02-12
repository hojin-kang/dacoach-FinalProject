package com.dacoach.mapper.challenge;

import java.util.HashMap;
import java.util.List;

import com.dacoach.model.challenge.ChallengeDTO;

public interface ChallengeMapper {
	public List<ChallengeDTO> getAllChallenges(int user_idx);
	public int insertDefaultChallenges(int user_idx);
	public HashMap<String, Object> getScores(int user_idx);
	public int achieveChallenge(ChallengeDTO challenge);
	public void addPoints(ChallengeDTO challenge);
}
