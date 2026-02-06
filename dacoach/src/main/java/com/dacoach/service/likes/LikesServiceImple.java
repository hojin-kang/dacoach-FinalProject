package com.dacoach.service.likes;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.likes.LikesMapper;
import com.dacoach.model.likes.LikesClassDTO;
import com.dacoach.model.likes.LikesUserDTO;

@Service
public class LikesServiceImple implements LikesService {

	@Autowired
	private LikesMapper likesMapper;

	@Override
	public int addLikesClass(Integer class_idx, Integer user_idx) throws Exception {
		int result = likesMapper.addLikesClass(class_idx, user_idx);
		return result;
	}

	@Override
	public int delLikesClass(Integer class_idx, Integer user_idx) throws Exception {
		int result = likesMapper.delLikesClass(class_idx, user_idx);
		return result;
	}

	@Override
	public boolean isLikedClass(Integer class_idx, Integer user_idx) throws Exception {
		return likesMapper.isLikedClass(class_idx, user_idx) > 0;
	}

	@Override
	public List<LikesUserDTO> getLikedCoaches(Integer user_idx) throws Exception {
		return likesMapper.getLikedCoaches(user_idx);
	}

	@Override
	public List<LikesClassDTO> getLikedClasses(Integer user_idx) throws Exception {
		return likesMapper.getLikedClasses(user_idx);
	}

	@Override
	public int delLikesUser(Integer liked_user_idx, Integer user_idx) throws Exception {
		int result = likesMapper.delLikesUser(liked_user_idx, user_idx);
		return result;
	}
}
