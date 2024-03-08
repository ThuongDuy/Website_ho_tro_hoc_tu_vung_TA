package com.elearning.studyvocabulary.model.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.elearning.studyvocabulary.model.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer>, JpaSpecificationExecutor<Users>{
	Users findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	Page<Users> findByUsernameContainingIgnoreCase(String keyword, Pageable pageable);
	Users findByEmail(String email);
}
