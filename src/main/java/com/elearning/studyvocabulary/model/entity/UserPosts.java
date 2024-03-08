package com.elearning.studyvocabulary.model.entity;

import java.util.Date;
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

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name="UserPosts")
@Data
public class UserPosts {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="userPostsId")
	private int userPostsId;
	
	@Column(name="content")
	private String content;
	
	@Column(name="postingDate")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date postingDate;
	
	@Column(name="status")
	private boolean status=true;
	
	@OneToMany(mappedBy = "userPosts", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Comments> listComments;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Users.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private Users user;
	
	public void addComment(Comments comments) {
		listComments.add(comments);
	}
	public void removeComment(Comments comments) {
		listComments.remove(comments);
	}
}
