package com.elearning.studyvocabulary.model.seviceImp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearning.studyvocabulary.model.entity.ERole;
import com.elearning.studyvocabulary.model.entity.Roles;
import com.elearning.studyvocabulary.model.repository.RoleRepository;
import com.elearning.studyvocabulary.model.service.RoleService;

@Service
public class RoleServiceImp implements RoleService{
	@Autowired
	private RoleRepository roleRepository;
	@Override
	public Optional<Roles> findByRoleName(ERole roleName) {
		return roleRepository.findByRoleName(roleName);
	}
	@Override
	public List<Roles> getAllRole() {
		return roleRepository.findAll();
	}
}
