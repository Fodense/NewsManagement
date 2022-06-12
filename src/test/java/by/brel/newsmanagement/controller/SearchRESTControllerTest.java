package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.dto.NewsDto;
import by.brel.newsmanagement.service.SearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchRESTController.class)
public class SearchRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    private NewsDto news;
    private NewsDto news2;

    @Before
    public void init() {
        news = new NewsDto(1L, getNowDateTime(), "Test", "Test", new ArrayList<>());
        news2 = new NewsDto(2L, getNowDateTime(), "Test2", "Test2", new ArrayList<>());
    }

    @Test
    public void searchNews() throws Exception {
        List<NewsDto> newsDtoList = List.of(news, news2);

        given(searchService.searchNews(Mockito.anyString())).willReturn(newsDtoList);

        mockMvc.perform(get("/api/v1/news/search")
                        .param("keyword", Mockito.anyString())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idNews", is(news.getIdNews().intValue())))
                .andExpect(jsonPath("$[0].dateCreatedNews", is(news.getDateCreatedNews().toString())))
                .andExpect(jsonPath("$[0].title", is(news.getTitle())))
                .andExpect(jsonPath("$[0].text", is(news.getText())))
                .andExpect(jsonPath("$[0].commentList", hasSize(0)))
                .andExpect(jsonPath("$[1].idNews", is(news2.getIdNews().intValue())))
                .andExpect(jsonPath("$[1].dateCreatedNews", is(news2.getDateCreatedNews().toString())))
                .andExpect(jsonPath("$[1].title", is(news2.getTitle())))
                .andExpect(jsonPath("$[1].text", is(news2.getText())))
                .andExpect(jsonPath("$[1].commentList", hasSize(0)));

        verify(searchService, times(1)).searchNews(Mockito.anyString());
        verifyNoMoreInteractions(searchService);
    }

    public LocalDateTime getNowDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(dateFormatter);

        return LocalDateTime.parse(dateTime, dateFormatter);
    }
}