package com.intellisense.analyser.web.controllers;

import com.intellisense.analyser.web.model.SearchRequest;
import com.intellisense.analyser.web.model.SearchResponse;
import com.intellisense.analyser.web.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@Validated
@RestController()
@RequestMapping(value = "/counter-api")
class ApplicationController {

    final private SearchService searchService;

    @Autowired
    public ApplicationController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(value = "/search", method = POST)
    public ResponseEntity getTextCounts(@RequestBody SearchRequest searchRequest) {
        Map counts = searchService.searchTexts(searchRequest.getSearchText());
        return new ResponseEntity<>(new SearchResponse(counts), OK);
    }

    @RequestMapping(value = "/top/{count}", method = GET)
    public ResponseEntity fetchMaxOccurringWords(@PathVariable("count") @Valid @Min(0) Integer topCount) {
        List maxWordsWithCount = searchService.fetchMaxOccurringWords(topCount);
        return new ResponseEntity<>(maxWordsWithCount, OK);
    }
}
