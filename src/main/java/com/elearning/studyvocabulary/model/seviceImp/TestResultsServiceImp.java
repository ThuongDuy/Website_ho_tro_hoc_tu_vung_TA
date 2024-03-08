package com.elearning.studyvocabulary.model.seviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.elearning.studyvocabulary.model.entity.Test;
import com.elearning.studyvocabulary.model.entity.TestResults;
import com.elearning.studyvocabulary.model.entity.Users;
import com.elearning.studyvocabulary.model.repository.TestResultsRepository;
import com.elearning.studyvocabulary.model.service.TestResultsService;
@Service
public class TestResultsServiceImp implements TestResultsService{
	@Autowired
	private TestResultsRepository resultsRepository;
	@Override
	public TestResults findByTestAndUser(Test test, Users users) {
			return resultsRepository.findByTestAndUser(test, users);
	}
	@Override
	public TestResults save(TestResults testResults) {
		return resultsRepository.save(testResults);
	}
	@Override
	public Page<TestResults> findByTest(Test test, Pageable pageable) {
		return resultsRepository.findByTest(test, pageable);
	}

}
