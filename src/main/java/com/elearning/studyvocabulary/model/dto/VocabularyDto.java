package com.elearning.studyvocabulary.model.dto;

import com.elearning.studyvocabulary.model.entity.Vocabulary;

import lombok.Data;

@Data
public class VocabularyDto {
	private int vocabularyId;
	private String vocab;
	private String means;
	private String fromType;
	private String example;
	private String illustration;
	public VocabularyDto() {
		super();
	}
	public VocabularyDto(int vocabularyId, String vocab, String means, String fromType, String example,
			String illustration) {
		super();
		this.vocabularyId = vocabularyId;
		this.vocab = vocab;
		this.means = means;
		this.fromType = fromType;
		this.example = example;
		this.illustration = illustration;
	}
	
	public static VocabularyDto from(Vocabulary vocabulary) {
		VocabularyDto dto = new VocabularyDto();
		dto.setVocabularyId(vocabulary.getVocabularyId());
		dto.setVocab(vocabulary.getVocab());
		dto.setMeans(vocabulary.getMeans());
		dto.setFromType(vocabulary.getFromType());
		dto.setExample(vocabulary.getExample());
		dto.setIllustration(vocabulary.getIllustration());
		return dto;
	}

}
