package com.elearning.studyvocabulary.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.elearning.studyvocabulary.model.entity.AnhViet;

public interface AnhVietService {
	Page<AnhViet> findByWordContainingIgnoreCase(String keyword, Pageable pageable);
	AnhViet getById(int id);
	Page<AnhViet> findWord(Pageable pageable);
	Page<AnhViet> findByWord(String word, Pageable pageable);
}
