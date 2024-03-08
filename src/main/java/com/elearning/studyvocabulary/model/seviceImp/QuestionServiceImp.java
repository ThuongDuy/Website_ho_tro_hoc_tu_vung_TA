package com.elearning.studyvocabulary.model.seviceImp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.elearning.studyvocabulary.exceptions.NotFoundException;
import com.elearning.studyvocabulary.model.entity.Question;
import com.elearning.studyvocabulary.model.entity.Test;
import com.elearning.studyvocabulary.model.repository.QuestionRepository;
import com.elearning.studyvocabulary.model.service.QuestionService;

@Service
public class QuestionServiceImp implements QuestionService{
	
	@Autowired
	private QuestionRepository questionRepository;
	
	
	@Override
	public boolean deleteQuestion(int id) {
		Optional<Question> questionDB = questionRepository.findById(id);
		if (questionDB.isPresent()) {
			Question question = questionDB.get();
			question.setTest(null);
			questionRepository.delete(question);
			return true;
		} else {
			throw new NotFoundException("Not Found Question By Id: " + id);
		}
	}

	@Override
	public Page<Question> findQuestion(Pageable pageable) {
		Page<Question> pageQuestion = questionRepository.findAll(pageable);
		return pageQuestion;
	}

	@Override
	public Question save(Question question) {
		return questionRepository.save(question);
	}


	@Override
	public Question getQuestionById(int questionId) {
		return questionRepository.findById(questionId)
				.orElseThrow(() -> new NotFoundException("Not Found Question By Id: " + questionId));
	}

	@Override
	public Question updateQuestion(int questionId, Question question) {
		Optional<Question> questionDB = questionRepository.findById(questionId);
		if (questionDB.isPresent()) {
			Question questionUpdate = questionDB.get();
			questionUpdate.setQuestionNumber(question.getQuestionNumber());;
			questionUpdate.setContent(question.getContent());
			questionUpdate.setOption_1(question.getOption_1());
			questionUpdate.setOption_2(question.getOption_2());
			questionUpdate.setOption_3(question.getOption_3());
			questionUpdate.setOption_4(question.getOption_4());
			questionUpdate.setCorrectAnswer(question.getCorrectAnswer());
			Question questionUpdated = questionRepository.save(questionUpdate);
			return questionUpdated;
		} else {
			throw new NotFoundException("Not Found Topic By Id: " + questionId);
		}
	}

	@Override
	public Page<Question> findByTestAndQuestionNumberContainingIgnoreCase(Test test, String keyword,
			Pageable pageable) {
		return questionRepository.findByTestAndQuestionNumberContainingIgnoreCase(test, keyword, pageable);
	}

	@Override
	public Page<Question> findByTest(Test test, Pageable pageable) {
		return questionRepository.findByTest(test, pageable);
	}

}
