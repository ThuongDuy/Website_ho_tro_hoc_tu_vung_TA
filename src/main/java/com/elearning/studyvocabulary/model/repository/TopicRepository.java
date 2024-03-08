package com.elearning.studyvocabulary.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.elearning.studyvocabulary.model.entity.Course;
import com.elearning.studyvocabulary.model.entity.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer>, JpaSpecificationExecutor<Topic>{
	Topic findByLevel(int level);
	Page<Topic> findByCourseAndTopicNameContainingIgnoreCase(Course course,String keyword, Pageable pageable);
	Page<Topic> findByCourse(Course course, Pageable pageable);
}
