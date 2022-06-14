package by.brel.newsmanagement.service.impl;

import by.brel.newsmanagement.dto.NewsDto;
import by.brel.newsmanagement.entity.News;
import by.brel.newsmanagement.exception_handling.exception.NoSuchDataException;
import by.brel.newsmanagement.mapper.MapperNews;
import by.brel.newsmanagement.repository.NewsRepository;
import by.brel.newsmanagement.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private MapperNews mapperNews;

    @Override
    public List<NewsDto> searchNews(String keyWord) {
        if (keyWord == null) {
            throw new NoSuchDataException("Keyword cannot be empty");
        }

        List<News> newsList = newsRepository.findAllByTitleContainingOrTextContaining(keyWord, keyWord);

        if (newsList.isEmpty()) {
            throw new NoSuchDataException("No matches were found");
        }

        return newsList.stream()
                .map(news -> mapperNews.convertNewsToNewsDto(news))
                .collect(Collectors.toList());
    }
}
