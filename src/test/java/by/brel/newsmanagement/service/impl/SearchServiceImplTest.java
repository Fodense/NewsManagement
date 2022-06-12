package by.brel.newsmanagement.service.impl;

import by.brel.newsmanagement.entity.News;
import by.brel.newsmanagement.repository.NewsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchServiceImplTest {

    private NewsRepository newsRepository;

    private LocalDateTime localDateTime;
    private News news;

    private List<News> newsList;

    @Before
    public void init() {
        newsRepository = mock(NewsRepository.class);

        localDateTime = LocalDateTime.now();
        news = new News(1L, localDateTime, "Test", "Test", new ArrayList<>());

        newsList = new ArrayList<>();
        newsList.add(news);
    }

    @Test
    public void searchNews() {
        when(newsRepository.findAllByTitleContainingOrTextContaining(anyString(), anyString())).thenReturn(newsList);

        List<News> newsList2 = newsRepository.findAllByTitleContainingOrTextContaining(anyString(), anyString());

        assertEquals(1, newsList2.size());
    }
}