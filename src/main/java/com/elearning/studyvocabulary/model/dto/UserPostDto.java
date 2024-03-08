package com.elearning.studyvocabulary.model.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.elearning.studyvocabulary.model.entity.UserPosts;

import lombok.Data;

@Data
public class UserPostDto {
	private int userPostsId;
	private String content;
	private Date postingDate;
	private boolean status;
	private List<CommentsDto> listComments;
	private UserDto user;
	
	public static UserPostDto from(UserPosts posts) {
		UserPostDto dto = new UserPostDto();
		dto.setUserPostsId(posts.getUserPostsId());
		dto.setContent(posts.getContent());
		dto.setPostingDate(posts.getPostingDate());
		dto.setStatus(posts.isStatus());
		List<CommentsDto> list = posts.getListComments().stream()
				.map(CommentsDto::from).collect(Collectors.toList());
		dto.setListComments(list);
		dto.setUser(UserDto.from(posts.getUser()));
		return dto;
	}
	
}
