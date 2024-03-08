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
@Table(name="Feedback")
@Data
public class Feedback {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="feedbackId")
	private int feedbackId;
	
	@Column(name="errorLocation")
	private String errorLocation;
	
	@Column(name="errorType")
	private String errorType;
	
	@Column(name="status")
	private boolean status=true;
	
	@Column(name="detail")
	private String detail;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Users.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private Users user;

}
