package com.intellisense.analyser.web.model;

import java.util.Map;

public class SearchResponse {

    private Map counts;

    public SearchResponse(Map counts) {
        this.counts = counts;
    }

    public Map getCounts() {
        return counts;
    }
}
