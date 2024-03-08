package com.elearning.studyvocabulary.model.entity;

import java.util.ArrayList;
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

import com.elearning.studyvocabulary.model.dto.TopicDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Topic")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="topicId")
	private int topicId;
	
	@Column(name="level", nullable = false)
	private int level;
	
	@Column(name="topicName", nullable = false)
	private String topicName;
	
	@OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Vocabulary> listVocab;
	
	@OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserProgression> listUserProgression;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Course.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "courseId")
	private Course course;
	
	public List<Vocabulary> listVocabularyLearned(int progress){
		List<Vocabulary> list = new ArrayList<Vocabulary>();
		for (int i=0;i<progress;i++) {
			list.add(this.listVocab.get(i));
		}
		return list;
	}
	public List<Vocabulary> listVocabularyUnlearned(int progress){
		List<Vocabulary> list = new ArrayList<Vocabulary>();
		for (int i=progress;i<this.listVocab.size();i++) {
			list.add(this.listVocab.get(i));
		}
		return list;
	}
	
	public void addVocabulary(Vocabulary vocabulary) {
		listVocab.add(vocabulary);
	}
	public void removeVocabulary(Vocabulary vocabulary) {
		listVocab.remove(vocabulary);
	}
	public static Topic from(TopicDto dto) {
		Topic topic = new Topic();
		topic.setTopicId(dto.getTopicId());
		topic.setLevel(dto.getLevel());
		topic.setTopicName(dto.getTopicName());
//		topic.setListVocab(dto.getListVocab());
		return topic;
	}

}
