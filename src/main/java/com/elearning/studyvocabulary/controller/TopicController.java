package com.elearning.studyvocabulary.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.studyvocabulary.model.dto.TopicDto;
import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.Vocabulary;
import com.elearning.studyvocabulary.payload.response.BaseResponse;
import com.elearning.studyvocabulary.model.service.TopicService;
import com.elearning.studyvocabulary.payload.request.Pagination;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/topic")
public class TopicController {
	@Autowired
	private TopicService topicService;
	
	@PostMapping ("/page")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> findTopic(@RequestBody Pagination pagination ) {
	    try {      
	      Order order = new Order(Sort.Direction.ASC,"level");
	      Pageable paging = PageRequest.of(pagination.getPage(), pagination.getSize(),Sort.by(order));
	      Page<Topic> pageTopic = topicService.findTopic(paging);
	      
	      List<Topic> listTopic = pageTopic.getContent();
	      List<TopicDto> listTopicDto = listTopic.stream()
					.map(TopicDto::from).collect(Collectors.toList());
	      
	      Map<String, Object> response = new HashMap<>();
	      response.put("content", listTopicDto);
	      response.put("currentPage", pageTopic.getNumber());
	      response.put("totalItems", pageTopic.getTotalElements());
	      response.put("totalPages", pageTopic.getTotalPages());
	      return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (Exception e) {
	    	Map<String, Object> response = new HashMap<>();
	    	response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.toString());
	      return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<String> createTopic(@RequestBody TopicDto topic) {
		Topic t =Topic.from(topic);
		topicService.save(t);
		return new ResponseEntity<String>("Record saved successfully",HttpStatus.CREATED);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping ("/delete/{id}")
	public boolean deleteTopicById(@PathVariable("id") int id) {
		return topicService.deleteTopic(id);
	}
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> getTopicById(@PathVariable("id") int id) {
		BaseResponse response = new BaseResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
				TopicDto.from(topicService.getTopicByTopicId(id)));
        return ResponseEntity.ok(response);
    }
	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> updateTpoic(@PathVariable("id") int id, @RequestBody TopicDto topic) {
		Topic t =Topic.from(topic);
        BaseResponse response = new BaseResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
        		TopicDto.from(topicService.updateTopic(id, t)));
        return ResponseEntity.ok(response);
    }
	@PutMapping("/addVocabulary/level={level}")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> updateAuthor(@PathVariable("level") int level, @RequestBody Vocabulary vocabulary) {
        BaseResponse response = new BaseResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
        		TopicDto.from(topicService.addVocabularyToTopic(level, vocabulary)));
        return ResponseEntity.ok(response);
    }
}
