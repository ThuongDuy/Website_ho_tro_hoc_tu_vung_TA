package com.elearning.studyvocabulary.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.elearning.studyvocabulary.model.entity.Test;
import com.elearning.studyvocabulary.model.entity.TestResults;
import com.elearning.studyvocabulary.model.entity.Users;

@Repository
public interface TestResultsRepository extends JpaRepository<TestResults, Integer> , JpaSpecificationExecutor<TestResults>{
	TestResults findByTestAndUser(Test test, Users users);
	Page<TestResults> findByTest(Test test, Pageable pageable);
}
