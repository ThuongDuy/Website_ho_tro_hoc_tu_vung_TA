package com.elearning.studyvocabulary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.studyvocabulary.model.entity.UserPosts;
import com.elearning.studyvocabulary.model.service.UserPostsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/user/post/")
public class PostController {
	
	@Autowired
	private UserPostsService postsService;
	
	@PreAuthorize("hasRole('USER')")
	@PutMapping("/create/userId={id}")
	public ResponseEntity<String> createPost(@RequestBody UserPosts post,@PathVariable("id") int id) {
		postsService.createPostByUser(post, id);
		return new ResponseEntity<String>("Record saved successfully",HttpStatus.CREATED);
	}
	
}
