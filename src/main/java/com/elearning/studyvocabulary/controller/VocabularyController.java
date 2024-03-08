package com.elearning.studyvocabulary.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.elearning.studyvocabulary.model.dto.VocabularyDto;
import com.elearning.studyvocabulary.model.entity.Vocabulary;
import com.elearning.studyvocabulary.model.service.VocabularyService;
import com.elearning.studyvocabulary.payload.response.BaseResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/vocabulary/")
public class VocabularyController {
	@Autowired
	private VocabularyService vocabularyService;
	
	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> createVocabulary(@Valid @RequestBody Vocabulary vocabulary) {
		vocabularyService.saveVocabulary(vocabulary);
		return new ResponseEntity<String>("Record saved successfully",HttpStatus.CREATED);
	}
	@GetMapping("/{id}")
    public ResponseEntity<Vocabulary> getVocabulary(@PathVariable("id") int id) {
		Vocabulary a = vocabularyService.getVocabulary(id);
        return ResponseEntity.ok(a);
    }
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping ("/delete/{id}")
	public ResponseEntity<String> deleteTopicById(@PathVariable("id") int id) {
		vocabularyService.deleteVocabulary(id);
		return new ResponseEntity<String>("Record delete successfully",HttpStatus.ACCEPTED);
	}
	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> updateVocabulary(@PathVariable("id") int id, @RequestBody Vocabulary vocabulary) {
        BaseResponse response = new BaseResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
        		VocabularyDto.from(vocabularyService.editVocabulary(id, vocabulary)));
        return ResponseEntity.ok(response);
    }

}
