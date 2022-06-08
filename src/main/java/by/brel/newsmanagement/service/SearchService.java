package by.brel.newsmanagement.service;

import by.brel.newsmanagement.dto.NewsDto;

import java.util.List;

public interface SearchService {

    List<NewsDto> searchNews(String keyWord);
}
