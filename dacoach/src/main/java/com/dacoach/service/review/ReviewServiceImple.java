package com.dacoach.service.review;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.review.ReviewMapper;
import com.dacoach.model.review.ReviewClassDTO;
import com.dacoach.model.review.ReviewCoachDTO;

@Service
public class ReviewServiceImple implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public String getUserType(int user_idx) {
        return reviewMapper.selectUserType(user_idx);
    }

    @Override
    public List<ReviewClassDTO> getWrittenClassReviews(int user_idx) {
    	List<ReviewClassDTO> list = reviewMapper.selectWrittenClassReviews(user_idx);
        fillTagList2(list);
        return list;
    }

    @Override
    public List<ReviewCoachDTO> getWrittenCoachReviews(int user_idx) {
    	List<ReviewCoachDTO> list = reviewMapper.selectWrittenCoachReviews(user_idx);
        fillTagList(list);
        return list;
    }

    @Override
    public List<ReviewClassDTO> getReceivedClassReviewsByProvider(int provider_idx) {
    	List<ReviewClassDTO> list = reviewMapper.selectReceivedClassReviewsByProvider(provider_idx);
    	fillTagList2(list);
    	return list;
    }

    @Override
    public List<ReviewCoachDTO> getReceivedCoachReviews(int user_idx) {
    	List<ReviewCoachDTO> list = reviewMapper.selectReceivedCoachReviews(user_idx);
        fillTagList(list);
        return list;
    }

    @Override
    public int deleteCoachReview(int review_coach_idx, int user_idx) {
        String type = getUserType(user_idx);
        if (!"coach".equals(type)) return 0;
        return reviewMapper.deleteCoachReview(review_coach_idx, user_idx);
    }

    @Override
    public int deleteClassReview(int review_class_idx, int user_idx) {
        String type = getUserType(user_idx);
        if (!"coach".equals(type)) return 0;
        return reviewMapper.deleteClassReview(review_class_idx, user_idx);
    }

	@Override
	public List<ReviewCoachDTO> getCoachReviews(int target_idx) {
		List<ReviewCoachDTO> reviews = reviewMapper.getCoachReviews(target_idx);
		return reviews;
	}
	

	//  REVIEW_COACH.TAG 문자열 -> tagList로 변환
	private void fillTagList(List<ReviewCoachDTO> reviews) {
	    if (reviews == null) return;

	    for (ReviewCoachDTO r : reviews) {
	        String tag = r.getTag();

	        if (tag == null || tag.isBlank()) {
	            r.setTagList(Collections.emptyList());
	            continue;
	        }

	        // "#" 기준 분리
	        String[] parts = tag.split("#");

	        List<String> list = new ArrayList<>();
	        for (String p : parts) {
	            if (!p.isBlank()) {
	                list.add("#" + p.trim());  // 다시 # 붙여서 화면 그대로 출력
	            }
	        }

	        r.setTagList(list);
	    }
	}
	
	private void fillTagList2(List<ReviewClassDTO> reviews) {
	    if (reviews == null) return;

	    for (ReviewClassDTO r : reviews) {
	        String tag = r.getTag();

	        if (tag == null || tag.isBlank()) {
	            r.setTagList(Collections.emptyList());
	            continue;
	        }

	        // "#" 기준 분리
	        String[] parts = tag.split("#");

	        List<String> list = new ArrayList<>();
	        for (String p : parts) {
	            if (!p.isBlank()) {
	                list.add("#" + p.trim());  // 다시 # 붙여서 화면 그대로 출력
	            }
	        }

	        r.setTagList(list);
	    }
	}

	@Override
	public List<ReviewCoachDTO> getCoachReviewsPaged(int coachUserIdx, int start, int end) {
	    List<ReviewCoachDTO> reviews = reviewMapper.getCoachReviewsPaged(coachUserIdx, start, end);
	    fillTagList(reviews);
	    for (ReviewCoachDTO r : reviews) {
	        System.out.println("RAW TAG = " + r.getTag());
	        System.out.println("TAG LIST = " + r.getTagList());
	    }
	    return reviews;
	}

	
	@Override
	public int getCoachReviewCount(int coachUserIdx) {
		return reviewMapper.getCoachReviewCount(coachUserIdx);
	}
}
