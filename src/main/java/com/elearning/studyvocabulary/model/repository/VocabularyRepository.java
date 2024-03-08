package com.elearning.studyvocabulary.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.Vocabulary;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Integer>,JpaSpecificationExecutor<Vocabulary>{
	Page<Vocabulary> findByTopic(Topic topic, Pageable pageable);
	Page<Vocabulary> findByTopicAndVocabContainingIgnoreCase(Topic topic,String keyword, Pageable pageable);
}
