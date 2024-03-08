package com.elearning.studyvocabulary.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.elearning.studyvocabulary.model.entity.Question;

import lombok.Data;

@Data
public class QuestionDto {
	
	private int questionId;
	private String questionNumber;
	private String content;
	private String option_1;
	private String option_2;
	private String option_3;
	private String option_4;
	private String correctAnswer;
	
	public QuestionDto() {
		super();
	}


	public QuestionDto(int questionId, String questionNumber, String content, String option_1, String option_2,
			String option_3, String option_4, String correctAnswer) {
		super();
		this.questionId = questionId;
		this.questionNumber = questionNumber;
		this.content = content;
		this.option_1 = option_1;
		this.option_2 = option_2;
		this.option_3 = option_3;
		this.option_4 = option_4;
		this.correctAnswer = correctAnswer;
	}
	
	
	public static QuestionDto from(Question question) {
		QuestionDto dto = new QuestionDto();
		dto.setQuestionId(question.getQuestionId());
		dto.setQuestionNumber(question.getQuestionNumber());
		dto.setContent(question.getContent());
		dto.setOption_1(question.getOption_1());
		dto.setOption_2(question.getOption_2());
		dto.setOption_3(question.getOption_3());
		dto.setOption_4(question.getOption_4());
		dto.setCorrectAnswer(question.getCorrectAnswer());
		return dto;
	}




}
