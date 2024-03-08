package com.elearning.studyvocabulary.model.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.elearning.studyvocabulary.model.entity.Question;
import com.elearning.studyvocabulary.model.entity.Test;

public interface QuestionService {
	boolean deleteQuestion(int id);
	Page<Question> findQuestion(Pageable pageable);
//	List<Question> getAllQuestion();
	Question save(Question question);
	Question getQuestionById(int questionId);
	Question updateQuestion(int questionId, Question question);
	Page<Question> findByTestAndQuestionNumberContainingIgnoreCase(Test test,String keyword, Pageable pageable);
	Page<Question> findByTest(Test test, Pageable pageable);

}
