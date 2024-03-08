package com.elearning.studyvocabulary.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VocabularyResponse {
	private int vocabularyId;
	private String vocab;
	private String means;
	private String example;
	private String illustration;
}
