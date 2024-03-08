package com.elearning.studyvocabulary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.studyvocabulary.model.service.QuestionService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/app/test")
public class QuestionController {
	
	@Autowired
	private QuestionService questionSevice;

}
