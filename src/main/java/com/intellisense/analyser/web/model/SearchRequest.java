package com.intellisense.analyser.web.model;

import java.util.List;

public class SearchRequest {
    private List<String> searchText;

    public SearchRequest() {
    }

    public SearchRequest(List<String> searchText) {
        this.searchText = searchText;
    }

    public List<String> getSearchText() {
        return searchText;
    }
}
