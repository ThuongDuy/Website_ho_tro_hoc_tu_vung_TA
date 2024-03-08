package com.elearning.studyvocabulary.model.entity;

import java.util.Date;

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

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name="Comments")
@Data
public class Comments {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="commentsId")
	private int commentsId;
	
	@Column(name="content")
	private String content;
	
	@Column(name="postingDate")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date postingDate;
	
	@Column(name="status")
	private boolean status=true;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Users.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private Users user;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = UserPosts.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "userPostsId")
	private UserPosts userPosts;

}
