package com.elearning.studyvocabulary.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.elearning.studyvocabulary.model.entity.Course;
import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.Vocabulary;


public interface TopicService {
	List<Topic> getAllTopic();
	boolean deleteTopic(int id);
	Page<Topic> findTopic(Pageable pageable);
	Topic save(Topic topic);
	Topic addVocabularyToTopic(int level,Vocabulary vocabulary );
	Topic getTopicByTopicId(int topicId);
	Topic updateTopic(int topicId, Topic topic);
	Page<Topic> findByCourseAndTopicNameContainingIgnoreCase(Course course,String keyword, Pageable pageable);
	Page<Topic> findByCourse(Course course, Pageable pageable);
}
