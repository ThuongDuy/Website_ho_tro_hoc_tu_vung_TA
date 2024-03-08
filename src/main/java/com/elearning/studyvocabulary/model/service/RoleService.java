package com.elearning.studyvocabulary.model.service;

import java.util.List;
import java.util.Optional;

import com.elearning.studyvocabulary.model.entity.ERole;
import com.elearning.studyvocabulary.model.entity.Roles;


public interface RoleService {
	Optional<Roles> findByRoleName(ERole roleName);
	List<Roles> getAllRole();
}
