package com.elearning.studyvocabulary.model.seviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.elearning.studyvocabulary.exceptions.NotFoundException;
import com.elearning.studyvocabulary.model.entity.AnhViet;
import com.elearning.studyvocabulary.model.repository.AnhVietRepository;
import com.elearning.studyvocabulary.model.service.AnhVietService;

@Service
public class AnhVietServiceImp implements AnhVietService{
	@Autowired
	private AnhVietRepository anhVietRepository;
	@Override
	public Page<AnhViet> findByWordContainingIgnoreCase(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return anhVietRepository.findByWordContainingIgnoreCase(keyword, pageable);
	}

	@Override
	public AnhViet getById(int id) {
		// TODO Auto-generated method stub
		return anhVietRepository.findById(id).orElseThrow(() -> new NotFoundException("Not Found By Id: " + id));
	}

	@Override
	public Page<AnhViet> findWord(Pageable pageable) {
		return anhVietRepository.findAll(pageable);
	}

	@Override
	public Page<AnhViet> findByWord(String word, Pageable pageable) {
		// TODO Auto-generated method stub
		return anhVietRepository.findByWord(word, pageable);
	}

}
