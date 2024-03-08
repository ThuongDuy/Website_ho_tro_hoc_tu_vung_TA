package com.elearning.studyvocabulary.model.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.elearning.studyvocabulary.model.entity.Course;
import com.elearning.studyvocabulary.model.entity.Test;
import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.Users;


public interface CourseService {
	boolean deleteCourse(int id);
	Page<Course> findCourse(Pageable pageable);
	Course save(Course course);
	Course getCourseById(int courseId);
	Course updateCourse(int courseId, Course course);
	Page<Course> findByCourseNameContainingIgnoreCase(String keyword, Pageable pageable);
	List<Course> getAll();
	long countCourse();
}
