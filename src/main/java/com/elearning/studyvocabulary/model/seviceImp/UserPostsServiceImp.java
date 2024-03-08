package com.elearning.studyvocabulary.model.seviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearning.studyvocabulary.model.entity.Comments;
import com.elearning.studyvocabulary.model.entity.UserPosts;
import com.elearning.studyvocabulary.model.entity.Users;
import com.elearning.studyvocabulary.model.repository.CommentsRepository;
import com.elearning.studyvocabulary.model.repository.UserPostsRepository;
import com.elearning.studyvocabulary.model.repository.UserRepository;
import com.elearning.studyvocabulary.model.service.UserPostsService;

@Service
public class UserPostsServiceImp implements UserPostsService{
	@Autowired
	private UserPostsRepository postsRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentsRepository commentsRepository;
	
	@Override
	public UserPosts createPostByUser(UserPosts post, int userId) {
		Users user = userRepository.findById(userId).get();
		post.setUser(user);
		return postsRepository.save(post);
	}

	@Override
	public UserPosts addCommentToPost(int postId, Comments comments, int userId) {
		UserPosts post = postsRepository.findById(postId).get();
		post.addComment(comments);
		comments.setUserPosts(post);
		commentsRepository.save(comments);
		return post;
	}
	

}
