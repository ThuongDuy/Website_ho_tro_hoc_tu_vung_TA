package com.elearning.studyvocabulary.model.entity;


import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="Course")
@Data
public class Course {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="courseId")
	private int courseId;
	
	@Column(name="courseName", nullable = false, unique = true)
	private String courseName;
	@Column(name="introduce")
	private String introduce;
	@Column(name="illustration")
	private String illustration;
	@ManyToMany(mappedBy = "listCourse",fetch=FetchType.LAZY)
	private List<Users> listUser;
	
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Topic> listTopic;
	
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Test> listTest;
	

}
