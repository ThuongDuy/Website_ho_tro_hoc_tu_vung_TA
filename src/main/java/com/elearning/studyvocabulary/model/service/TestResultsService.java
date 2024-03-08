package com.elearning.studyvocabulary.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.elearning.studyvocabulary.model.entity.Test;
import com.elearning.studyvocabulary.model.entity.TestResults;
import com.elearning.studyvocabulary.model.entity.Users;

public interface TestResultsService {
	TestResults findByTestAndUser(Test test, Users users);
	TestResults save(TestResults testResults);
	Page<TestResults> findByTest(Test test, Pageable pageable);
}
