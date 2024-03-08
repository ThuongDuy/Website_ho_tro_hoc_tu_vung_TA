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
@Table(name="TestResults")
@Data
public class TestResults {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="testResultsId")
	private int testResultsId;
	
	@Column(name="scores")
	private float scores;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Users.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private Users user;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Test.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "testId")
	private Test test;

}
