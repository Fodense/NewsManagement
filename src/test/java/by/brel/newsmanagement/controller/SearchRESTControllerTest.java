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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        news = new NewsDto(1L, LocalDateTime.now(), "Test", "Test", new ArrayList<>());
        news2 = new NewsDto(2L, LocalDateTime.now(), "Test2", "Test2", new ArrayList<>());
    }

    @Test
    public void searchNews() throws Exception {
        List<NewsDto> newsDtoList = List.of(news, news2);

        given(searchService.searchNews(Mockito.any())).willReturn(newsDtoList);

        mockMvc.perform(get("/api/v1/news/search")
                        .param("keyword", Mockito.anyString())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idNews", is(1)))
                .andExpect(jsonPath("$[1].idNews", is(2)));

        verify(searchService, times(1)).searchNews(Mockito.any());
    }
}