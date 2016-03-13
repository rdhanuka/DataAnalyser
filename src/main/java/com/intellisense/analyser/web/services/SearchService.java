package com.intellisense.analyser.web.services;

import com.intellisense.analyser.web.repository.ParagraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;


@Component
public class SearchService {

    final private ParagraphRepository repository;

    @Autowired
    public SearchService(ParagraphRepository repository) {
        this.repository = repository;
    }

    public Map<String, Integer> searchTexts(List<String> searchTexts) {
        return searchTexts.stream()
                .distinct()
                .collect(toMap(textKey -> textKey, (String text) -> {
                    Integer count = repository.getOrderedWordCounts().get(text.toLowerCase());
                    return count == null ? 0 : count;
                }));
    }

    public List<String> fetchMaxOccurringWords(Integer topCount) {
        return repository.getOrderedWordCounts().entrySet().stream()
                .limit(topCount)
                .map(value -> value.getKey().concat("|").concat(value.getValue().toString()))
                .collect(toList());
    }

}
