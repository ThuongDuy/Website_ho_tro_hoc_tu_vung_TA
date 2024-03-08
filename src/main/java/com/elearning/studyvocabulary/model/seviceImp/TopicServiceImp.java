package com.elearning.studyvocabulary.model.seviceImp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.elearning.studyvocabulary.exceptions.NotFoundException;
import com.elearning.studyvocabulary.model.entity.Course;
import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.UserProgression;
import com.elearning.studyvocabulary.model.entity.Users;
import com.elearning.studyvocabulary.model.entity.Vocabulary;
import com.elearning.studyvocabulary.model.repository.TopicRepository;
import com.elearning.studyvocabulary.model.repository.UserProgressionRepository;
import com.elearning.studyvocabulary.model.repository.UserRepository;
import com.elearning.studyvocabulary.model.repository.VocabularyRepository;
import com.elearning.studyvocabulary.model.service.TopicService;
import com.elearning.studyvocabulary.model.service.VocabularyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TopicServiceImp implements TopicService {

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private VocabularyService vocabularyService;
	
	@Autowired
	private UserProgressionRepository progressionRepository;
	@Override
	public List<Topic> getAllTopic() {
		return topicRepository.findAll();
	}

	@Override
	public boolean deleteTopic(int id) {
		Optional<Topic> topicDB = topicRepository.findById(id);
		if (topicDB.isPresent()) {
			Topic topic = topicDB.get();
			for(Vocabulary v: topic.getListVocab()) {
				vocabularyService.deleteVocabulary(v.getVocabularyId());
			}
			
			for(UserProgression p: topic.getListUserProgression()) {
				p.setUser(null);
				p.setTopic(null);
				progressionRepository.delete(p);
			}
			topic.setListUserProgression(null);
			topicRepository.save(topic);
			topic.setCourse(null);
			topicRepository.delete(topic);
			return true;
		} else {
			throw new NotFoundException("Not Found Topic By Id: " + id);
		}
	}

	@Override
	public Page<Topic> findTopic(Pageable pageable) {
		Page<Topic> pageTopic = topicRepository.findAll(pageable);
		return pageTopic;
	}

	@Override
	public Topic save(Topic topic) {
		Topic topicCreated = topicRepository.save(topic);
		return topicCreated;
	}

	@Override
	public Topic addVocabularyToTopic(int level, Vocabulary vocabulary) {
		Topic topic = topicRepository.findByLevel(level);
		topic.addVocabulary(vocabulary);
		vocabulary.setTopic(topic);
		vocabularyService.saveVocabulary(vocabulary);
		return topic;
	}

	@Override
	public Topic getTopicByTopicId(int topicId) {
		Topic topic = topicRepository.findById(topicId)
				.orElseThrow(() -> new NotFoundException("Not Found Topic By Id: " + topicId));
		return topic;
	}

	@Override
	public Topic updateTopic(int topicId, Topic topic) {
		Optional<Topic> topicDB = topicRepository.findById(topicId);
		if (topicDB.isPresent()) {
			Topic topicUpdate = topicDB.get();
			topicUpdate.setLevel(topic.getLevel());
			topicUpdate.setTopicName(topic.getTopicName());
			Topic topicUpdated = topicRepository.save(topicUpdate);
			return topicUpdated;
		} else {
			throw new NotFoundException("Not Found Topic By Id: " + topicId);
		}
	}

	@Override
	public Page<Topic> findByCourseAndTopicNameContainingIgnoreCase(Course course, String keyword, Pageable pageable) {
		return topicRepository.findByCourseAndTopicNameContainingIgnoreCase(course, keyword, pageable);
	}

	@Override
	public Page<Topic> findByCourse(Course course, Pageable pageable) {
		return topicRepository.findByCourse(course, pageable);
	}

}
