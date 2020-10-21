package com.edu.search.service;


import com.edu.common.bean.SearchResult;

public interface SearchService {
    SearchResult search(String queryString, Integer page, Integer rows);
}
