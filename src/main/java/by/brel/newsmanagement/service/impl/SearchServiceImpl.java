package by.brel.newsmanagement.service.impl;

import by.brel.newsmanagement.dto.NewsDto;
import by.brel.newsmanagement.entity.News;
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
        List<News> news = newsRepository.findAllByTitleContainingOrTextContaining(keyWord, keyWord);

        return news.stream()
                .map(news2 -> mapperNews.convertNewsToNewsDto(news2))
                .collect(Collectors.toList());
    }
}
