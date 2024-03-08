package com.elearning.studyvocabulary.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonFormat;


import lombok.Data;

@Entity
@Table(name="Users")
@Data
public class Users implements Serializable{
	private static final long serialVersionUID = -6500665823330706018L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="userId")
	private int userId;
	
	@Column(name = "username", length = 100, nullable = false, unique = true)
	private String username;
	
	@Column(name="password", nullable = false, length = 60)
    private String password;
	
	@Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;
	
	@Column(name="created")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date created;
	
	@Column(name="fullName")
	private String fullName;
	
	@Column(name="address")
	private String address;
	
	@Column(name="avatar")
	private String avatar;
	
	@Column(name="userStatus")
	private boolean userStatus;
	
	@Column(name="learningMode")
	private int learningMode = 5;
	
	@Column(name="reviewAgainMode")
	private int reviewAgainMode=10;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name="user_role",joinColumns = @JoinColumn(name="userId"),
			inverseJoinColumns = @JoinColumn(name="roleId"))
	private Set<Roles> listRoles = new HashSet<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserProgression> listUserProgression;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<TestResults> listTestResults;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="user_story",joinColumns = @JoinColumn(name="userId"),
			inverseJoinColumns = @JoinColumn(name="storyId"))
	private List<BilingualStory> listBilingualStories;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Comments> listComments;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="user_course",joinColumns = @JoinColumn(name="userId"),
			inverseJoinColumns = @JoinColumn(name="CourseId"))
	private List<Course> listCourse;
	
	
}
