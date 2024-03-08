package com.elearning.studyvocabulary.model.seviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.elearning.studyvocabulary.exceptions.NotFoundException;
import com.elearning.studyvocabulary.model.entity.BilingualStory;
import com.elearning.studyvocabulary.model.repository.BilingualStoryRepository;
import com.elearning.studyvocabulary.model.service.BilingualStoryService;

@Service
public class BilingualStoryServiceImp implements BilingualStoryService{
	@Autowired
	private BilingualStoryRepository storyRepository;
	
	@Override
	public Page<BilingualStory> findByCategoryContainingIgnoreCase(String keyword, Pageable pageable) {
		return storyRepository.findByCategoryContainingIgnoreCase(keyword, pageable);
	}

	@Override
	public BilingualStory getById(int id) {
		// TODO Auto-generated method stub
		return storyRepository.findById(id).orElseThrow(() -> new NotFoundException("Not Found By Id: " + id));
	}


	@Override
	public Page<BilingualStory> findBilingualStory(Pageable pageable) {
		// TODO Auto-generated method stub
		return storyRepository.findAll(pageable);
	}

	@Override
	public void delete(int id) {
		storyRepository.deleteById(id);
	}

	@Override
	public BilingualStory save(BilingualStory bilingualStory) {
		return storyRepository.save(bilingualStory);
	}

	@Override
	public long countAll() {
		return storyRepository.count();
	}

}
