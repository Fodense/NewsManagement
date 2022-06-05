package by.brel.newsmanagement.service.impl;

import by.brel.newsmanagement.entity.News;
import by.brel.newsmanagement.repository.NewsRepository;
import by.brel.newsmanagement.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public List<News> searchNews(String keyWord) {
        return newsRepository.findAllByTitleContainingOrTextContaining(keyWord, keyWord);
    }
}
