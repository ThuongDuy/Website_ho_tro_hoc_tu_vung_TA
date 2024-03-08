package com.elearning.studyvocabulary.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.elearning.studyvocabulary.model.entity.Test;
import com.elearning.studyvocabulary.model.entity.TestResults;

import lombok.Data;

@Data
public class TestDto {
	private int testId;
	private String testNumber;
	private List<QuestionDto> listQuestion;
	private List<TestResultsDto> listTestResults;
	
	public TestDto() {
		super();
	}
	
	public TestDto(int testId, String testNumber, List<QuestionDto> listQuestion, List<TestResultsDto> listTestResults ) {
		super();
		this.testId = testId;
		this.testNumber = testNumber;
		this.listQuestion = listQuestion;
		this.listTestResults = listTestResults;
	}

	public static TestDto from(Test test) {
		TestDto dto = new TestDto();
		dto.setTestId(test.getTestId());
		dto.setTestNumber(test.getTestNumber());
		List<QuestionDto> list = test.getListQuestion().stream()
				.map(QuestionDto::from).collect(Collectors.toList());
		dto.setListQuestion(list);
		List<TestResultsDto> listTest = test.getListTestResults().stream()
				.map(TestResultsDto::from).collect(Collectors.toList());
		dto.setListTestResults(listTest);
		return dto;
	}
	public List<TestResultsDto> sortResults(List<TestResultsDto> results){
		for (int i = 0 ; i <results.size()-1;i++) {
			for(int j=i+1;j<results.size();j++) {
				if(results.get(i).getScores()<results.get(j).getScores()) {
					TestResultsDto t =results.get(i);
					results.set(i, results.get(j));
					results.set(j, t);
				}
			}
		}
		return results;
	}
}
