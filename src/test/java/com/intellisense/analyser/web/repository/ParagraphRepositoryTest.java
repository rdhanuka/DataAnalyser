package com.intellisense.analyser.web.repository;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;

@RunWith(MockitoJUnitRunner.class)
public class ParagraphRepositoryTest {

    private static ParagraphRepository paragraphRepository;

    @Rule
    public ExpectedException expectedException = none();

    private static final LinkedHashMap EXPECTED_WORDS_WITH_COUNTS = new LinkedHashMap<String, Integer>() {{
        put("test", 2);
        put("paragraph", 1);
        put("in", 1);
        put("this", 1);
        put("used", 1);
        put("is", 1);
    }};

    @BeforeClass
    public static void setUp() {
        paragraphRepository = new ParagraphRepository("sampleParagraph.txt");
    }

    @Test
    public void shouldReturnWordsWithTheirCounts() {
        LinkedHashMap<String,Integer> wordsWithCount = paragraphRepository.getOrderedWordCounts();
        assertThat(wordsWithCount).containsAllEntriesOf(EXPECTED_WORDS_WITH_COUNTS);
        assertThat(wordsWithCount.size()).isEqualTo(EXPECTED_WORDS_WITH_COUNTS.size());
    }

    @Test
    public void shouldReturnWordsWithTheirCountsOnDescendingOrder() {
        LinkedHashMap wordsWithCount = paragraphRepository.getOrderedWordCounts();
        List<Integer> actualOrderedValues = (ArrayList<Integer>) wordsWithCount.values().stream().collect(toList());
        List<Integer> expectedOrder = asList(2, 1, 1, 1, 1, 1);
        assertThat(actualOrderedValues).isEqualTo(expectedOrder);
    }

    @Test
    public void shouldFailFastForDataSourceNotLoaded() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("paragraph file is not being loaded, stopping application...");
        new ParagraphRepository("missing$$%.txt").getOrderedWordCounts();
    }

}