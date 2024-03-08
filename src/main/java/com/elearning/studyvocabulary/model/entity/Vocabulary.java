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
@Table(name="Vocabulary")
@Data
public class Vocabulary {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="vocabularyId")
	private int vocabularyId;
	
	@Column(name="vocab",nullable = false)
	private String vocab;
	
	@Column(name="means",nullable = false)
	private String means;
	
	@Column(name="fromType")
	private String fromType;
	
	@Column(name="example")
	private String example;
	
	@Column(name="illustration")
	private String illustration;
	
	@Column(name="transcription")
	private String transcription;
	
	@Column(name="pronunciation")
	private String pronunciation;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Topic.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "topicId")
	private Topic topic;
	
}
