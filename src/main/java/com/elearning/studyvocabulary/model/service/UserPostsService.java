package com.elearning.studyvocabulary.model.service;

import com.elearning.studyvocabulary.model.entity.Comments;
import com.elearning.studyvocabulary.model.entity.UserPosts;

public interface UserPostsService {
	UserPosts createPostByUser(UserPosts post ,int userId);
	UserPosts addCommentToPost(int postId,Comments comments,int userId);

}
