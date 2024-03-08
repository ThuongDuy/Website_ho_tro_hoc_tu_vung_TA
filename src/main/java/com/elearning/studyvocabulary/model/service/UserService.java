package com.elearning.studyvocabulary.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.elearning.studyvocabulary.model.entity.Users;

public interface UserService {
	Users findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	Page<Users> findByUsernameContainingIgnoreCase(String keyword, Pageable pageable);
	Users save(Users user);
	List<Users> getAllUser();
	Page<Users> findUser(Pageable pageable);
	boolean deleteUser(int idUser);
	Users findById(int idUser);
	Users update(int id, Users user);
	Users findByEmail(String email);
	Users setup(int learn, int review);
	long countUser();
}
