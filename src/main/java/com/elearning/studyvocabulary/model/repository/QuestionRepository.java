package com.elearning.studyvocabulary.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.elearning.studyvocabulary.model.entity.Question;
import com.elearning.studyvocabulary.model.entity.Test;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer>,JpaSpecificationExecutor<Question>{
	Page<Question> findByTestAndQuestionNumberContainingIgnoreCase(Test test,String keyword, Pageable pageable);
	Page<Question> findByTest(Test test, Pageable pageable);
}
