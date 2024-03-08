package com.elearning.studyvocabulary.payload.response;

import java.util.List;

import com.elearning.studyvocabulary.model.dto.UserProgressionDto;

import lombok.Data;

@Data
public class JwtResponse {
	private String token;
	private String type="Bearer";
	private String username;
	private String email;
	private String avatar;
	private int learningMode;
	private int reviewAgainMode;
	private List<String> listRoles;
	private List<UserProgressionDto> listUserProgression;
	

	public JwtResponse(String token, String username, String email, String avatar,
			int learningMode, int reviewAgainMode, List<String> listRoles,
			List<UserProgressionDto> listUserProgression) {
		super();
		this.token = token;
		this.username = username;
		this.email = email;
		this.avatar = avatar;
		this.learningMode = learningMode;
		this.reviewAgainMode = reviewAgainMode;
		this.listRoles = listRoles;
		this.listUserProgression = listUserProgression;
	}

	
}
