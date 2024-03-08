package com.elearning.studyvocabulary.model.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.elearning.studyvocabulary.model.entity.Users;

import lombok.Data;

@Data
public class UserDto {
	private int userId;
	private String username;
	private String email;
	private Date created;
	private String avatar;
	private boolean userStatus;
	private int learningMode;
	private int reviewAgainMode;
	private List<UserProgressionDto> listUserProgression;
	public UserDto() {
		super();
	}
	public UserDto(int userId, String username, String email, Date created, String avatar, boolean userStatus,
			int learningMode, int reviewAgainMode, List<UserProgressionDto> listUserProgression) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.created = created;
		this.avatar = avatar;
		this.userStatus = userStatus;
		this.learningMode = learningMode;
		this.reviewAgainMode = reviewAgainMode;
		this.listUserProgression = listUserProgression;
	}
	
	public static UserDto from(Users user) {
		UserDto dto = new UserDto();
		dto.setUserId(user.getUserId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setCreated(user.getCreated());
		dto.setAvatar(user.getAvatar());
		dto.setUserStatus(user.isUserStatus());
		dto.setLearningMode(user.getLearningMode());
		dto.setReviewAgainMode(user.getReviewAgainMode());
		List<UserProgressionDto> userProgressionDto = user.getListUserProgression().stream()
				.map(UserProgressionDto::from).collect(Collectors.toList());
		dto.setListUserProgression(userProgressionDto);
		return dto;
	}
}
