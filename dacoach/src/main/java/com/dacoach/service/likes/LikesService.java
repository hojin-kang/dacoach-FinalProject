package com.dacoach.service.likes;

import java.util.*;

import com.dacoach.model.likes.LikesClassDTO;
import com.dacoach.model.likes.LikesUserDTO;

public interface LikesService {
	
	int addLikesClass(Integer class_idx, Integer user_idx) throws Exception;
	
	int delLikesClass(Integer class_idx, Integer user_idx) throws Exception;
	
	boolean isLikedClass(Integer class_idx, Integer user_idx) throws Exception;
	
	List<LikesUserDTO> getLikedCoaches(Integer user_idx) throws Exception;
	List<LikesClassDTO> getLikedClasses(Integer user_idx) throws Exception;

	int delLikesUser(Integer liked_user_idx, Integer user_idx) throws Exception;

}
