package com.intellisense.analyser.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellisense.analyser.web.model.SearchRequest;
import com.intellisense.analyser.web.model.SearchResponse;
import com.intellisense.analyser.web.services.SearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@RunWith(MockitoJUnitRunner.class)
public class ApplicationControllerTest{

    private MockMvc mockMvc;

    @Mock
    private SearchService searchService;

    @InjectMocks
    private ApplicationController controller;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldReturnTextsWithTheirCount() throws Exception {

        final List<String> searchTexts = asList("hello", "find");
        final SearchRequest request = new SearchRequest(searchTexts);
        final HashMap<String,Integer> mockedData = new HashMap(){{put("hello", 22);put("find",44 );}};
        final SearchResponse counts = new SearchResponse(mockedData);
        when(searchService.searchTexts(searchTexts)).thenReturn(mockedData);

        mockMvc.perform(post("/counter-api/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(request)))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.counts", is(counts.getCounts())));
        verify(searchService).searchTexts(searchTexts);
    }

    @Test
    public void shouldReturnMaxOccurrenceWords() throws Exception {
        final Integer maxResult  = 3;
        final List<String> expectedResponse = asList("hello|22", "find|44");

        when(searchService.fetchMaxOccurringWords(maxResult)).thenReturn(expectedResponse);

        mockMvc.perform(get("/counter-api/top/{count}", maxResult)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));

        verify(searchService).fetchMaxOccurringWords(maxResult);
    }


}
