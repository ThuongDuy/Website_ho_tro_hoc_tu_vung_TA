package com.elearning.studyvocabulary.model.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.elearning.studyvocabulary.model.entity.Feedback;
import com.elearning.studyvocabulary.model.entity.Users;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer>, JpaSpecificationExecutor<Feedback>{
	int countByStatus(boolean status);
	Page<Feedback> findByErrorLocationContainingIgnoreCaseOrErrorTypeContainingIgnoreCase(Pageable pageable,String errorLocation, String errorType);
	List<Feedback> findByUser(Users user);
}
