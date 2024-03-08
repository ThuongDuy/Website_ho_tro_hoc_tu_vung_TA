package com.elearning.studyvocabulary.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearning.studyvocabulary.model.entity.ERole;
import com.elearning.studyvocabulary.model.entity.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer>{
	Optional<Roles> findByRoleName(ERole roleName);
}
