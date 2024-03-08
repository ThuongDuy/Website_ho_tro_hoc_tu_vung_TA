package com.elearning.studyvocabulary.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.elearning.studyvocabulary.model.entity.Course;
import com.elearning.studyvocabulary.model.entity.Question;
import com.elearning.studyvocabulary.model.entity.Test;
import com.elearning.studyvocabulary.model.entity.Users;



public interface TestService {
	
	List<Test> getAllTest();
	boolean deleteTest(int id);
	Page<Test> findTest(Pageable pageable);
	Test save(Test test);
	Test getTestByTestId(int testId);
	Test updateTest(int testId, Test test);
	List<Users> highResultsOnTest(Test test);
	Page<Test> findByCourseAndTestNumberContainingIgnoreCase(Course course,String keyword, Pageable pageable);
	Page<Test> findByCourse(Course course, Pageable pageable);

}
