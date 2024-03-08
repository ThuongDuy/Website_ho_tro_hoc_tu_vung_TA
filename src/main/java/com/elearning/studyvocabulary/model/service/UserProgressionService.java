package com.elearning.studyvocabulary.model.service;


import java.util.List;

import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.UserProgression;
import com.elearning.studyvocabulary.model.entity.Users;

public interface UserProgressionService {
	UserProgression saveUserProgression(UserProgression userProgression);
	UserProgression findByTopicAndUser(Topic topic, Users user);
	List<UserProgression> findByTopic(Topic topic);
	List<UserProgression> findByUser(Users user);
	UserProgression update(int id);
	boolean delete (UserProgression userProgression);
}
