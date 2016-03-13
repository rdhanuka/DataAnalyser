package com.intellisense.analyser.web.services;

import com.intellisense.analyser.web.repository.ParagraphRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceTest {

    private SearchService searchService;
    @Mock
    private ParagraphRepository repository;

    final static LinkedHashMap WORD_COUNTS = new LinkedHashMap() {{
        put("hello", 22);
        put("feugiat", 12);
        put("laven", 11);
        put("habitant", 8);
    }};

    @Before
    public void setUp() {
        when(repository.getOrderedWordCounts()).thenReturn(WORD_COUNTS);
        searchService = new SearchService(repository);
    }


    @Test
    public void shouldReturnSearchTextWithTheirCounts() {
        Map textWithCount = searchService.searchTexts(asList("feugiat","laven"));

        Assertions.assertThat(WORD_COUNTS).containsAllEntriesOf(textWithCount);
        Assertions.assertThat(textWithCount).containsOnlyKeys("feugiat", "laven");
    }

    @Test
    public void shouldReturnCountOfZeroForUnknownSearchText() {
        Map textWithCount = searchService.searchTexts(asList("unknown","laven"));
        Assertions.assertThat(textWithCount.get("unknown")).isEqualTo(0);
    }

    @Test
    public void shouldHandleDuplicateSearchTexts() {
        Map textWithCount = searchService.searchTexts(asList("habitant", "habitant"));

        Assertions.assertThat(textWithCount).containsOnlyKeys("habitant");
        Assertions.assertThat(textWithCount.get("habitant")).isEqualToComparingFieldByField(WORD_COUNTS.get("habitant"));
    }

    @Test
    public void shouldReturnEmptyResponseForNoSearchTexts() {
        Map textWithCount = searchService.searchTexts(emptyList());
        Assertions.assertThat(textWithCount).isEmpty();
    }

    @Test
    public void shouldReturnTopNCountedWordsInParagraph() {
        int top = 2;
        List textWithCount = searchService.fetchMaxOccurringWords(top);
        Assertions.assertThat(textWithCount).isEqualTo(asList("hello|22", "feugiat|12"));
    }

    @Test
    public void shouldReturnAllWordsForTopExceedingNumberOfWordsInParagraph() {
        List textWithCount = searchService.fetchMaxOccurringWords(Integer.MAX_VALUE);
        Assertions.assertThat(textWithCount).isEqualTo(asList("hello|22", "feugiat|12", "laven|11", "habitant|8"));
    }

}