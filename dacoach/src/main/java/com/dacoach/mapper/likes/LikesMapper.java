package com.dacoach.mapper.likes;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.model.likes.LikesClassDTO;
import com.dacoach.model.likes.LikesUserDTO;

@Mapper
public interface LikesMapper {
	
	int addLikesClass(Integer class_idx, Integer user_idx) throws Exception;
	
	int delLikesClass(Integer class_idx, Integer user_idx) throws Exception;
	
	int isLikedClass(Integer class_idx, Integer user_idx) throws Exception;
	
	List<LikesUserDTO> getLikedCoaches(Integer user_idx);

    List<LikesClassDTO> getLikedClasses(Integer user_idx);
    
    int delLikesUser(Integer liked_user_idx, Integer user_idx) throws Exception;

}
