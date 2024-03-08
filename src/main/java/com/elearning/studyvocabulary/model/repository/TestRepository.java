package com.elearning.studyvocabulary.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.elearning.studyvocabulary.model.entity.Course;
import com.elearning.studyvocabulary.model.entity.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer>,  JpaSpecificationExecutor<Test>{
	Page<Test> findByCourseAndTestNumberContainingIgnoreCase(Course course,String keyword, Pageable pageable);
	Page<Test> findByCourse(Course course, Pageable pageable);
}
