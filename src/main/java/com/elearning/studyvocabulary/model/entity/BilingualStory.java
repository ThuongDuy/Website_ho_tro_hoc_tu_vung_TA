package com.elearning.studyvocabulary.model.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="BilingualStory")
@Data
public class BilingualStory {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="storyId")
	private int storyId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="englishName")
	private String englishName;
	
	@Column(name="category")
	private String category;
	
	@Column(name = "illustration")
	private String illustration;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "content")
	private String content;
	
	@ManyToMany(mappedBy = "listBilingualStories",fetch=FetchType.LAZY)
	private List<Users> listUser;
}
