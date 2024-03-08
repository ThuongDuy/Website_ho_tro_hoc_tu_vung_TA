package com.elearning.studyvocabulary.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.elearning.studyvocabulary.model.entity.AnhViet;

@Repository
public interface AnhVietRepository extends JpaRepository<AnhViet, Integer>, JpaSpecificationExecutor<AnhViet>{
	Page<AnhViet> findByWordContainingIgnoreCase(String keyword, Pageable pageable);
	@Query("SELECT t FROM AnhViet t WHERE LOWER(t.word) LIKE LOWER(CONCAT(?1,'%'))")
	Page<AnhViet> findByWord(String word, Pageable pageable);

}
