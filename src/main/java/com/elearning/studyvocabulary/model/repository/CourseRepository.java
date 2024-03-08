package com.elearning.studyvocabulary.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.elearning.studyvocabulary.model.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>, JpaSpecificationExecutor<Course>{
	Page<Course> findByCourseNameContainingIgnoreCase(String keyword, Pageable pageable);
}
