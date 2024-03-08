package com.elearning.studyvocabulary.model.seviceImp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.elearning.studyvocabulary.exceptions.NotFoundException;
import com.elearning.studyvocabulary.model.entity.Course;
import com.elearning.studyvocabulary.model.entity.Question;
import com.elearning.studyvocabulary.model.entity.Test;
import com.elearning.studyvocabulary.model.entity.TestResults;
import com.elearning.studyvocabulary.model.entity.Users;
import com.elearning.studyvocabulary.model.repository.QuestionRepository;
import com.elearning.studyvocabulary.model.repository.TestRepository;
import com.elearning.studyvocabulary.model.repository.TestResultsRepository;
import com.elearning.studyvocabulary.model.service.TestService;

@Service
public class TestServiceImp implements TestService{
	@Autowired
	private TestRepository testRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private TestResultsRepository resultsRepository;
	
	@Override
	public List<Test> getAllTest() {
		return testRepository.findAll();
	}

	@Override
	public boolean deleteTest(int id) {
		Optional<Test> testDB = testRepository.findById(id);
		if (testDB.isPresent()) {
			Test test = testDB.get();
			test.setCourse(null);
			for(Question q:test.getListQuestion()) {
				questionRepository.delete(q);
			}
			for(TestResults t:test.getListTestResults()) {
				resultsRepository.delete(t);
			}
			testRepository.delete(test);
			return true;
		} else {
			throw new NotFoundException("Not Found Test By Id: " + id);
		}
	}

	@Override
	public Page<Test> findTest(Pageable pageable) {
		Page<Test> pageTest = testRepository.findAll(pageable);
		return pageTest;
	}

	@Override
	public Test save(Test test) {
		Test testCreated = testRepository.save(test);
		return testCreated;
	}


	@Override
	public Test getTestByTestId(int testId) {
		Test test = testRepository.findById(testId)
				.orElseThrow(() -> new NotFoundException("Not Found Test By Id: " + testId));
		return test;
	}

	@Override
	public Test updateTest(int testId, Test test) {
		Optional<Test> testDB = testRepository.findById(testId);
		if (testDB.isPresent()) {
			Test testUpdate = testDB.get();
			testUpdate.setTestNumber(test.getTestNumber());
			Test testUpdated = testRepository.save(testUpdate);
			return testUpdated;
		} else {
			throw new NotFoundException("Not Found Test By Id: " + testId);
		}
	}

	@Override
	public List<Users> highResultsOnTest(Test test) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Test> findByCourseAndTestNumberContainingIgnoreCase(Course course, String keyword, Pageable pageable) {
		return testRepository.findByCourseAndTestNumberContainingIgnoreCase(course, keyword, pageable);
	}

	@Override
	public Page<Test> findByCourse(Course course, Pageable pageable) {
		return testRepository.findByCourse(course, pageable);
	}

}
