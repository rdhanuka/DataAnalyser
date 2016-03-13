package com.intellisense.analyser.web.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

@Component
public class ParagraphRepository {

    private final LinkedHashMap<String, Integer> orderedWordCounts;

    @Autowired
    public ParagraphRepository(@Value("${paragraph.source}") String filename) {
        orderedWordCounts = sortByValueDesc(getWordCounts(filename));
    }

    private Map<String, Integer> getWordCounts(String fileName) {
        try (Stream<String> lines = Files.lines(Paths.get(new ClassPathResource(fileName).getURI()))) {
            return lines
                    .parallel()
                    .flatMap(line -> stream(line.replaceAll("[^\\w|\\s]", "").toLowerCase().split("\\s")))
                    .collect(Collectors.toMap(key -> key, initialValue -> 1, Integer::sum));
        } catch (IOException e) {
            throw new RuntimeException("paragraph file is not being loaded, stopping application...",e.getCause());
        }
    }

    private LinkedHashMap<String, Integer> sortByValueDesc(Map<String, Integer> wordCountMap) {
        return wordCountMap.entrySet()
                .parallelStream()
                .sorted((c1, c2) -> c2.getValue().compareTo(c1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, value) -> value, LinkedHashMap::new));
    }

    public LinkedHashMap<String,Integer> getOrderedWordCounts() {
        return orderedWordCounts;
    }
}
