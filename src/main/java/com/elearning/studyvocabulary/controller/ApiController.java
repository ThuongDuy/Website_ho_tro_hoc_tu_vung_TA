package com.elearning.studyvocabulary.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.studyvocabulary.model.entity.Course;
import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.UserProgression;
import com.elearning.studyvocabulary.model.entity.Users;
import com.elearning.studyvocabulary.model.entity.Vocabulary;
import com.elearning.studyvocabulary.model.service.CourseService;
import com.elearning.studyvocabulary.model.service.TopicService;
import com.elearning.studyvocabulary.model.service.UserProgressionService;
import com.elearning.studyvocabulary.model.service.UserService;
import com.elearning.studyvocabulary.security.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api")
public class ApiController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserProgressionService progressionService;
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private CourseService courseService;
	
	@GetMapping("/test")
	public Object test(@RequestParam("courseId") Integer courseId,
			@RequestParam("topicId") Integer topicId) {
		Topic topic = topicService.getTopicByTopicId(topicId);
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		UserProgression progress = progressionService.findByTopicAndUser(topic, user);
		List<Vocabulary> list = new ArrayList<Vocabulary>();
		if(user.getLearningMode()<=topic.getListVocab().size()-progress.getProgress()) {
			list = topic.getListVocab().subList(progress.getProgress(), progress.getProgress()+user.getLearningMode());
		}else {
			list = topic.getListVocab().subList(progress.getProgress(), topic.getListVocab().size());
		}
		list = list.stream().map(item -> {
			item.setTopic(null);
            return item; 
        }).toList();
		return list;
	}
	
	@GetMapping("/review")
	public Object review(@RequestParam("courseId") Integer courseId) {
		Course course = courseService.getCourseById(courseId);
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		List<Vocabulary> list = new ArrayList<Vocabulary>();
		for (Topic i : course.getListTopic()) {
			UserProgression progress = progressionService.findByTopicAndUser(i, user);
			if(progress.getProgress()>0) {
				list.addAll(i.getListVocab().subList(0, progress.getProgress()));
			}
		}
        Random rand = new Random();
        List<Vocabulary> listReview = new ArrayList<Vocabulary>();
        if(list.size()>user.getReviewAgainMode()) {
	        for (int i = 0; i < user.getReviewAgainMode(); i++) {
	            int n = rand.nextInt(list.size());
	            listReview.add(list.get(n));
	            list.remove(n);
	        }
        }else {
        	listReview=list;
        	Collections.shuffle(listReview);
        }
        listReview = listReview.stream().map(item -> {
			item.setTopic(null);
            return item; 
        }).toList();
        System.out.println("abc"+listReview.size());
		return listReview;
	}
	@GetMapping("/course/{courseId}/topic/{topicId}/review") 
	public Object reviewAll(Model model, @PathVariable("courseId") Integer courseId,
			@PathVariable("topicId") Integer topicId) throws JsonProcessingException {
		Course course = courseService.getCourseById(courseId);
		Topic topic =topicService.getTopicByTopicId(topicId);
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Users user = userService.findByUsername(customUserDetails.getUsername());
		List<Vocabulary> list = new ArrayList<Vocabulary>();

		UserProgression progress = progressionService.findByTopicAndUser(topic, user);
		if(progress.getProgress()>0) {
			list= topic.getListVocab().subList(0, progress.getProgress());
		}
		
        Random rand = new Random();
        List<Vocabulary> listReview = new ArrayList<Vocabulary>();
        if(list.size()>user.getReviewAgainMode()) {
	        for (int i = 0; i < user.getReviewAgainMode(); i++) {
	            int n = rand.nextInt(list.size());
	            listReview.add(list.get(n));
	            list.remove(n);
	        }
        }else {
        	listReview=list;
        	Collections.shuffle(listReview);
        }
        listReview = listReview.stream().map(item -> {
			item.setTopic(null);
            return item; 
        }).toList();
        
        List<List<String>> list2 = new ArrayList<>();
        for(Vocabulary v: listReview) {
        	List<String> tam = new ArrayList<String>();
        	for(Topic t: course.getListTopic()) {
        		for(Vocabulary i:t.getListVocab()) {
        			tam.add(i.getMeans());
        		}
        	}
        	tam.remove(v.getMeans());
        	Collections.shuffle(tam);
        	List<String> tl = tam.subList(0, 3);
        	tl.add(v.getMeans());
        	Collections.shuffle(tl);
        	list2.add(tl);
        }
        
        Map<String, Object > result = new HashMap<>();
        result.put("listVocab", listReview);
        result.put("list2", list2);
		return result;
	}

}
