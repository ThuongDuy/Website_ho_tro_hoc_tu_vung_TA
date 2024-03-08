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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name="UserProgression")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProgression {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="userProgressionId")
	private int userProgressionId;
	
	@Column(name="progress")
	private int progress;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Users.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private Users user;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Topic.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "topicId")
	private Topic topic;

}
