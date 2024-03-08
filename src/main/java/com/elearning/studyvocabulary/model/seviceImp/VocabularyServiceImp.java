package com.elearning.studyvocabulary.model.seviceImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.elearning.studyvocabulary.exceptions.NotFoundException;
import com.elearning.studyvocabulary.model.entity.Topic;
import com.elearning.studyvocabulary.model.entity.Vocabulary;
import com.elearning.studyvocabulary.model.repository.VocabularyRepository;
import com.elearning.studyvocabulary.model.service.VocabularyService;

@Service
public class VocabularyServiceImp implements VocabularyService{
	@Autowired
	private VocabularyRepository vocabularyRepository;

	@Override
	public Vocabulary saveVocabulary(Vocabulary vocabulary) {
		Vocabulary vocab=vocabularyRepository.save(vocabulary);
//		vocab.setTopic(null);
		return vocab;
	}

	@Override
	public List<Vocabulary> findByTopicId(int topicId) {
		return vocabularyRepository.findAll(new Specification<Vocabulary>() {
            @Override
            public Predicate toPredicate(Root<Vocabulary> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("topicId"), topicId)));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
	}

	@Override
	public Vocabulary editVocabulary(int vocabularyId, Vocabulary vocabulary) {
		Vocabulary vocab = vocabularyRepository.findById(vocabularyId).get();
		vocab.setVocab(vocabulary.getVocab());
		vocab.setMeans(vocabulary.getMeans());
		vocab.setFromType(vocabulary.getFromType());
		vocab.setExample(vocabulary.getExample());
		vocab.setIllustration(vocabulary.getIllustration());
		vocab.setTranscription(vocabulary.getTranscription());
		vocab.setPronunciation(vocabulary.getPronunciation());
		vocabularyRepository.save(vocab);
		return vocab;
	}

	@Override
	public Vocabulary getVocabulary(int vocabularyId) {
		Vocabulary a = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new NotFoundException("Not Found Vocabulary By Id: " + vocabularyId));
		a.setTopic(null);
		return a;
	}

	@Override
	public boolean deleteVocabulary(int vocabularyId) {
		Optional<Vocabulary> vocabDB = vocabularyRepository.findById(vocabularyId);
		if (vocabDB.isPresent()) {
			Vocabulary vocab = vocabDB.get();
			vocab.setTopic(null);
			vocabularyRepository.deleteById(vocabularyId);
			return true;
		} else {
			throw new NotFoundException("Not Found Vocabulary By Id: " + vocabularyId);
		}
	}

	@Override
	public Page<Vocabulary> findByTopic(Topic topic, Pageable pageable) {
		return vocabularyRepository.findByTopic(topic, pageable);
	}

	@Override
	public Page<Vocabulary> findByTopicAndVocabContainingIgnoreCase(Topic topic, String keyword, Pageable pageable) {
		return vocabularyRepository.findByTopicAndVocabContainingIgnoreCase(topic, keyword, pageable);
	}

}
