package com.elearning.studyvocabulary.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.elearning.studyvocabulary.model.entity.BilingualStory;


public interface BilingualStoryService {
	Page<BilingualStory> findByCategoryContainingIgnoreCase(String keyword, Pageable pageable);
	BilingualStory getById(int id);
	Page<BilingualStory> findBilingualStory(Pageable pageable);
	void delete(int id);
	BilingualStory save(BilingualStory bilingualStory);
	long countAll();
}
