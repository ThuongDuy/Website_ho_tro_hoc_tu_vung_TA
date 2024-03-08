package com.elearning.studyvocabulary.model.dto;

import com.elearning.studyvocabulary.model.entity.TestResults;

import lombok.Data;

@Data
public class TestResultsDto {
	private int testResultsId;
	private float scores;
	private UserDto user;
	public TestResultsDto() {
		super();
	}
	public TestResultsDto(int testResultsId, float scores, UserDto user) {
		super();
		this.testResultsId = testResultsId;
		this.scores = scores;
		this.user = user;
	}
	
	public static TestResultsDto from(TestResults testResults) {
		TestResultsDto dto = new TestResultsDto();
		dto.setTestResultsId(testResults.getTestResultsId());
		dto.setScores(testResults.getScores());
		dto.setUser(UserDto.from(testResults.getUser()));
		return dto;
	}
}
