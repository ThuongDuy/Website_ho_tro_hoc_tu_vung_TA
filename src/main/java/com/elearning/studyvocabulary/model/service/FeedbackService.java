package com.elearning.studyvocabulary.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.elearning.studyvocabulary.model.entity.Feedback;

public interface FeedbackService {
	int countByStatus(boolean status);
	Page<Feedback> findFeedback(Pageable pageable);
	Feedback save(Feedback feedback);
	Feedback getFeedbackById(int id);
	Page<Feedback> findByErrorLocationContainingIgnoreCaseOrErrorTypeContainingIgnoreCase(Pageable pageable,String errorLocation, String errorType);
}
