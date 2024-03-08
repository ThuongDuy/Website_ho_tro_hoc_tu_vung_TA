package com.elearning.studyvocabulary.model.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.UserProgression;
import com.elearning.studyvocabulary.model.entity.Users;

@Repository
public interface UserProgressionRepository extends JpaRepository<UserProgression, Integer>, JpaSpecificationExecutor<UserProgression>{
	UserProgression findByTopicAndUser(Topic topic, Users user);
	List<UserProgression> findByTopic(Topic topic);
	List<UserProgression> findByUser(Users user);
}
