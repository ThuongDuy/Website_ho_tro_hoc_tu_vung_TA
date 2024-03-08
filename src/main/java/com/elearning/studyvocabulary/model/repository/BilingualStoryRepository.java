package com.elearning.studyvocabulary.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.elearning.studyvocabulary.model.entity.BilingualStory;

@Repository
public interface BilingualStoryRepository extends JpaRepository<BilingualStory, Integer>, JpaSpecificationExecutor<BilingualStory>{
	Page<BilingualStory> findByCategoryContainingIgnoreCase(String keyword, Pageable pageable);
}
