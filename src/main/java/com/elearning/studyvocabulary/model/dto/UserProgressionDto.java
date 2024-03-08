package com.elearning.studyvocabulary.model.dto;

import com.elearning.studyvocabulary.model.entity.UserProgression;

import lombok.Data;

@Data
public class UserProgressionDto {
	private int userProgressionId;
	private int progress;
	private TopicDto topic;
	
	public UserProgressionDto(int userProgressionId, int progress, TopicDto topic) {
		super();
		this.userProgressionId = userProgressionId;
		this.progress = progress;
	}

	public UserProgressionDto() {
		super();
	}
	
	public static UserProgressionDto from(UserProgression userProgression) {
		UserProgressionDto dto = new UserProgressionDto();
		dto.setUserProgressionId(userProgression.getUserProgressionId());
		dto.setProgress(userProgression.getProgress());
		dto.setTopic(TopicDto.from(userProgression.getTopic()));
		return dto;
	}
}
