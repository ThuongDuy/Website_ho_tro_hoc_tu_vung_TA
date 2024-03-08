package com.elearning.studyvocabulary.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.Vocabulary;

public interface VocabularyService {
	
	Vocabulary getVocabulary(int vocabularyId);
	Vocabulary saveVocabulary(Vocabulary vocabulary);
	Vocabulary editVocabulary(int vocabularyId, Vocabulary vocabulary);
	List<Vocabulary> findByTopicId(int topicId);
	boolean deleteVocabulary(int vocabularyId);
	Page<Vocabulary> findByTopic(Topic topic, Pageable pageable);
	Page<Vocabulary> findByTopicAndVocabContainingIgnoreCase(Topic topic,String keyword, Pageable pageable);

}
