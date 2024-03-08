package com.elearning.studyvocabulary.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearning.studyvocabulary.model.entity.UserPosts;

@Repository
public interface UserPostsRepository extends JpaRepository<UserPosts, Integer>{

}
