package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.dto.NewsDto;
import by.brel.newsmanagement.service.CommentService;
import by.brel.newsmanagement.service.NewsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(NewsRESTController.class)
public class NewsRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NewsService newsService;

    @MockBean
    private CommentService commentService;

    private NewsDto news;
    private NewsDto news2;
    private CommentDto comment;
    private CommentDto comment2;

    @Before
    public void init() {
        news = new NewsDto(1L, LocalDateTime.now(), "Test", "Test", new ArrayList<>());
        news2 = new NewsDto(2L, LocalDateTime.now(), "Test2", "Test2", new ArrayList<>());
        comment = new CommentDto(1L, LocalDateTime.now(), "Comment", "1", news.getIdNews());
        comment2 = new CommentDto(2L, LocalDateTime.now(), "Comment2", "2", news.getIdNews());
    }

    @Test
    public void getAllNews() throws Exception {
        List<NewsDto> newsDtoList = List.of(news, news2);

        given(newsService.getAllNewsPaginated(Mockito.any(PageRequest.class))).willReturn(newsDtoList);

        mockMvc.perform(get("/api/v1/news"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idNews", is(news.getIdNews().intValue())))
                .andExpect(jsonPath("$[0].title", is(news.getTitle())))
                .andExpect(jsonPath("$[0].text", is(news.getText())))
                .andExpect(jsonPath("$[0].commentList", hasSize(0)))
                .andExpect(jsonPath("$[1].idNews", is(news2.getIdNews().intValue())))
                .andExpect(jsonPath("$[1].title", is(news2.getTitle())))
                .andExpect(jsonPath("$[1].text", is(news2.getText())))
                .andExpect(jsonPath("$[1].commentList", hasSize(0)));

        verify(newsService, times(1)).getAllNewsPaginated(Mockito.any(Pageable.class));
    }

    @Test
    public void getNewsByID() throws Exception {
        given(newsService.getNewsByID(Mockito.anyLong())).willReturn(news);

        mockMvc.perform(get("/api/v1/news/{id}", news.getIdNews()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idNews", is(news.getIdNews().intValue())))
                .andExpect(jsonPath("$.title", is(news.getTitle())))
                .andExpect(jsonPath("$.text", is(news.getText())))
                .andExpect(jsonPath("$.commentList", hasSize(0)));

        verify(newsService, times(1)).getNewsByID(Mockito.anyLong());
    }

    @Test
    public void getAllCommentByIDNews() throws Exception {
        news.getCommentList().add(comment);
        news.getCommentList().add(comment2);

        given(newsService.findAllCommentsByIdNews(Mockito.anyLong(), Mockito.any(Pageable.class))).willReturn(news.getCommentList());

        mockMvc.perform(get("/api/v1/news/{idNews}/comments", news.getIdNews())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(comment))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idComment", is(comment.getIdComment().intValue())))
                .andExpect(jsonPath("$[0].text", is(comment.getText())))
                .andExpect(jsonPath("$[0].idUser", is(comment.getIdUser())))
                .andExpect(jsonPath("$[1].idComment", is(comment2.getIdComment().intValue())))
                .andExpect(jsonPath("$[1].text", is(comment2.getText())))
                .andExpect(jsonPath("$[1].idUser", is(comment2.getIdUser())));

        verify(newsService, times(1)).findAllCommentsByIdNews(Mockito.anyLong(), Mockito.any(Pageable.class));
    }

    @Test
    public void getCommentByIDWithIDNews() throws Exception {
        news.getCommentList().add(comment);

        given(newsService.getCommentByIDWithIDNews(Mockito.anyLong(), Mockito.anyLong())).willReturn(comment);

        mockMvc.perform(get("/api/v1/news/{idNews}/comments/{idComment}", news.getIdNews(), comment.getIdComment())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(comment))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idComment", is(comment.getIdComment().intValue())))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.idUser", is(comment.getIdUser())));

        verify(newsService, times(1)).getCommentByIDWithIDNews(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void saveNews() throws Exception {
        given(newsService.saveNews(Mockito.any(NewsDto.class))).willReturn(news);

        mockMvc.perform(post("/api/v1/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(news))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idNews", is(news.getIdNews().intValue())))
                .andExpect(jsonPath("$.title", is(news.getTitle())))
                .andExpect(jsonPath("$.text", is(news.getText())))
                .andExpect(jsonPath("$.commentList", hasSize(0)));

        verify(newsService, times(1)).saveNews(Mockito.any(NewsDto.class));
    }

    @Test
    public void saveCommentsByIDNews() throws Exception {
        news.getCommentList().add(comment);

        given(newsService.getNewsByID(Mockito.anyLong())).willReturn(news);
        given(commentService.saveComment(Mockito.any(CommentDto.class))).willReturn(comment);

        mockMvc.perform(post("/api/v1/news/{idNews}/comments", news.getIdNews())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(comment))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idComment", is(comment.getIdComment().intValue())))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.idUser", is(comment.getIdUser())));

        verify(newsService, times(1)).getNewsByID(Mockito.anyLong());
        verify(commentService, times(1)).saveComment(Mockito.any(CommentDto.class));
    }

    @Test
    public void updateNews() throws Exception {
        given(newsService.saveNews(Mockito.any(NewsDto.class))).willReturn(news);

        mockMvc.perform(put("/api/v1/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(news))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idNews", is(news.getIdNews().intValue())))
                .andExpect(jsonPath("$.title", is(news.getTitle())))
                .andExpect(jsonPath("$.text", is(news.getText())))
                .andExpect(jsonPath("$.commentList", hasSize(0)));

        verify(newsService, times(1)).saveNews(Mockito.any(NewsDto.class));
    }

    @Test
    public void updateCommentByIDNews() throws Exception {
        news.getCommentList().add(comment);

        given(newsService.getNewsByID(Mockito.anyLong())).willReturn(news);
        given(commentService.updateComment(Mockito.any(CommentDto.class), Mockito.any(CommentDto.class))).willReturn(comment);

        mockMvc.perform(put("/api/v1/news/{idNews}/comments", news.getIdNews())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(comment))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idComment", is(comment.getIdComment().intValue())))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.idUser", is(comment.getIdUser())));

        verify(newsService, times(1)).getNewsByID(Mockito.anyLong());
        verify(commentService, times(1)).updateComment(Mockito.any(CommentDto.class), Mockito.any(CommentDto.class));
    }

    @Test
    public void deleteNewsByID() throws Exception {
        when(newsService.deleteNews(Mockito.anyLong())).thenReturn("News is deleted");

        mockMvc.perform(delete("/api/v1/news/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uri", is(containsString("/api/v1/news/1"))))
                .andExpect(jsonPath("$.info", is(containsString("delete"))));

        verify(newsService, times(1)).deleteNews(Mockito.anyLong());
    }

    @Test
    public void deleteCommentByIDWithIDNews() throws Exception {
        when(commentService.deleteComment(Mockito.anyLong())).thenReturn("Comment is deleted");

        MvcResult requestResult = mockMvc.perform(delete("/api/v1/news/{idNews}/comments/{idComment}", 20, 200))
                .andExpect(status().isOk())
                .andReturn();

        String result = requestResult.getResponse().getContentAsString();
        assertEquals(result, "Comment is deleted");

        verify(commentService, times(1)).deleteComment(Mockito.anyLong());
    }
}