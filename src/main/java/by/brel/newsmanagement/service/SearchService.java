package by.brel.newsmanagement.service;

import by.brel.newsmanagement.entity.News;

import java.util.List;

public interface SearchService {

    List<News> searchNews(String keyWord);
}
