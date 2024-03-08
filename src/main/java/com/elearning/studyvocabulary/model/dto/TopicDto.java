package com.elearning.studyvocabulary.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.UserProgression;
import com.elearning.studyvocabulary.model.entity.Vocabulary;

import lombok.Data;

@Data
public class TopicDto {
	private int topicId;
	private int level;
	private String topicName;
	private List<VocabularyDto> listVocab;
//	private List<UserProgressionDTO> listUserProgression;
	public TopicDto() {
		super();
	}
	public TopicDto(int topicId, int level, String topicName, List<VocabularyDto> listVocab) {
		super();
		this.topicId = topicId;
		this.level = level;
		this.topicName = topicName;
		this.listVocab = listVocab;
	}
	
	
	public static TopicDto from(Topic topic) {
		TopicDto dto =new TopicDto();
		dto.setTopicId(topic.getTopicId());
		dto.setLevel(topic.getLevel());
		dto.setTopicName(topic.getTopicName());
		List<Vocabulary> vocabularys= topic.getListVocab();
		List<VocabularyDto> listVocabDto = vocabularys.stream()
				.map(VocabularyDto::from).collect(Collectors.toList());
		dto.setListVocab(listVocabDto);
//		List<UserProgression> userProgressions = topic.getListUserProgression();
//		List<UserProgressionDTO> listUserProgressionDto = userProgressions.stream()
//				.map(UserProgressionDTO::from).collect(Collectors.toList());
//		dto.setListUserProgression(listUserProgressionDto);
		return dto;
	}
}
