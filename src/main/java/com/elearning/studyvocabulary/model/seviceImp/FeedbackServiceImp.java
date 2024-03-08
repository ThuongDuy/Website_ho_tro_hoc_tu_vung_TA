package com.elearning.studyvocabulary.model.seviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.elearning.studyvocabulary.exceptions.NotFoundException;
import com.elearning.studyvocabulary.model.entity.Feedback;
import com.elearning.studyvocabulary.model.repository.FeedbackRepository;
import com.elearning.studyvocabulary.model.service.FeedbackService;

@Service
public class FeedbackServiceImp implements FeedbackService{
	@Autowired
	private FeedbackRepository feedbackRepository;
	@Override
	public int countByStatus(boolean status) {
		return feedbackRepository.countByStatus(status);
	}

	@Override
	public Page<Feedback> findFeedback(Pageable pageable) {
		return feedbackRepository.findAll(pageable);
	}

	@Override
	public Feedback save(Feedback feedback) {
		return feedbackRepository.save(feedback);
	}

	@Override
	public Page<Feedback> findByErrorLocationContainingIgnoreCaseOrErrorTypeContainingIgnoreCase(Pageable pageable,
			String errorLocation, String errorType) {
		return feedbackRepository.findByErrorLocationContainingIgnoreCaseOrErrorTypeContainingIgnoreCase(pageable, errorLocation, errorType);
	}

	@Override
	public Feedback getFeedbackById(int id) {
		Feedback feedback = feedbackRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Not Found By Id: " + id));
		return feedback;
	}

}
