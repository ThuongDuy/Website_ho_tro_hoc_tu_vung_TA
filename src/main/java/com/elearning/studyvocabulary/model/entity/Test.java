package com.elearning.studyvocabulary.model.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="Test")
@Data
public class Test {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="testId")
	private int testId;
	
	@Column(name="testNumber")
	private String testNumber;
	
	@Column(name="level")
	private String level;
	
	@OneToMany(mappedBy = "test", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Question> listQuestion;
	
	@OneToMany(mappedBy = "test", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<TestResults> listTestResults;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Course.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "courseId")
	private Course course;

}
