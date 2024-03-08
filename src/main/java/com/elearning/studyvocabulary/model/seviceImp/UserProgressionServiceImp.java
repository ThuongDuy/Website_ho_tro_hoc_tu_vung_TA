package com.elearning.studyvocabulary.model.seviceImp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearning.studyvocabulary.exceptions.NotFoundException;
import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.UserProgression;
import com.elearning.studyvocabulary.model.entity.Users;
import com.elearning.studyvocabulary.model.repository.UserProgressionRepository;
import com.elearning.studyvocabulary.model.service.UserProgressionService;

@Service
public class UserProgressionServiceImp implements UserProgressionService{
	
	@Autowired
	private UserProgressionRepository userProgressionRepository;

	@Override
	public UserProgression saveUserProgression(UserProgression userProgression) {
		return userProgressionRepository.save(userProgression);
		
	}

	@Override
	public UserProgression findByTopicAndUser(Topic topic, Users user) {
		return userProgressionRepository.findByTopicAndUser(topic, user);
	}

	@Override
	public List<UserProgression> findByTopic(Topic topic) {
		return userProgressionRepository.findByTopic(topic);
	}

	@Override
	public List<UserProgression> findByUser(Users user) {
		return userProgressionRepository.findByUser(user);
	}

	@Override
	public UserProgression update(int id) {
//		Optional<UserProgression> db = userProgressionRepository.findById(id);
//		if (db.isPresent()) {
//			UserProgression progression = db.get();
//			progression.
//			Topic topicUpdated = topicRepository.save(topicUpdate);
//			return topicUpdated;
//		} else {
//			throw new NotFoundException("Not Found Topic By Id: " + topicId);
//		}
		return null;
	}

	@Override
	public boolean delete(UserProgression userProgression) {
		userProgression.setTopic(null);
		userProgression.setUser(null);
		userProgressionRepository.delete(userProgression);
		return true;
	}


	
}
