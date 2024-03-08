package com.elearning.studyvocabulary.model.dto;


import java.util.Date;

import com.elearning.studyvocabulary.model.entity.Comments;

import lombok.Data;

@Data
public class CommentsDto {
	private int commentsId;
	private String content;
	private Date postingDate;
	private boolean status;
	private UserDto user;
	
	public static CommentsDto from(Comments comments) {
		CommentsDto dto =new CommentsDto();
		dto.setCommentsId(comments.getCommentsId());
		dto.setContent(comments.getContent());
		dto.setPostingDate(comments.getPostingDate());
		dto.setStatus(comments.isStatus());
		dto.setUser(UserDto.from(comments.getUser()));
		return dto;
	}
}
