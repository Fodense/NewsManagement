package by.brel.newsmanagement.service.impl;

import by.brel.newsmanagement.dto.NewsDto;
import by.brel.newsmanagement.entity.news.News;
import by.brel.newsmanagement.exception_handling.exception.NoSuchDataException;
import by.brel.newsmanagement.mapper.NewsMapper;
import by.brel.newsmanagement.repository.NewsRepository;
import by.brel.newsmanagement.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsMapper newsMapper;

    /**
     * Method for search info in table News by title and text fields
     * and convert news to newsDto
     * This method caches the result
     *
     * @param specification target search
     * @throws NoSuchDataException no matches found ib DB
     * @return result search
     */
    @Override
    @Cacheable(cacheNames = "news")
    public List<NewsDto> searchNews(Specification specification) {
        List<News> newsList = newsRepository.findAll(specification);

        if (newsList.isEmpty()) {
            throw new NoSuchDataException("No matches were found");
        }

        return newsList.stream()
                .map(news -> newsMapper.convertNewsToNewsDto(news))
                .collect(Collectors.toList());
    }
}
