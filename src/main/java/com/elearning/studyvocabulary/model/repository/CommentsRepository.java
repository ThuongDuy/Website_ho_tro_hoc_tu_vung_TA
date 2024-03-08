package com.elearning.studyvocabulary.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearning.studyvocabulary.model.entity.Comments;
@Repository
public interface CommentsRepository extends JpaRepository<Comments, Integer>{

}
