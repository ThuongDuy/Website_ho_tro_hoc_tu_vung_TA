package com.elearning.studyvocabulary.model.entity;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="Question")
@Data
public class Question {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="questionId")
	private int questionId;
	
	@Column(name="questionNumber")
	private String questionNumber;
	
	@Column(name="content")
	private String content;
	
	@Column(name="option_1")
	private String option_1;
	
	@Column(name="option_2")
	private String option_2;
	
	@Column(name="option_3")
	private String option_3;
	
	@Column(name="option_4")
	private String option_4;
	
	@Column(name="correctAnswer")
	private String correctAnswer;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Test.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "testId")
	private Test test;
	
}
