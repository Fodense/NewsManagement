package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.dto.NewsDto;
import by.brel.newsmanagement.exception_handling.exception.NoSuchCommentException;
import by.brel.newsmanagement.exception_handling.exception.NoSuchNewsException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
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
    public void getAllNews_success() throws Exception {
        List<NewsDto> newsDtoList = List.of(news, news2);

        given(newsService.getAllNewsPaginated(Mockito.any(PageRequest.class))).willReturn(newsDtoList);

        mockMvc.perform(get("/api/v1/news"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idNews", is(1)))
                .andExpect(jsonPath("$[0].title", is(news.getTitle())))
                .andExpect(jsonPath("$[0].text", is(news.getText())))
                .andExpect(jsonPath("$[0].commentList", hasSize(0)))
                .andExpect(jsonPath("$[1].idNews", is(2)))
                .andExpect(jsonPath("$[1].title", is(news2.getTitle())))
                .andExpect(jsonPath("$[1].text", is(news2.getText())))
                .andExpect(jsonPath("$[1].commentList", hasSize(0)));

        verify(newsService, times(1)).getAllNewsPaginated(Mockito.any(Pageable.class));
    }

    @Test
    public void getNewsByID_success() throws Exception {
        given(newsService.getNewsByID(Mockito.anyLong())).willReturn(news);

        mockMvc.perform(get("/api/v1/news/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idNews", is(1)))
                .andExpect(jsonPath("$.title", is(news.getTitle())))
                .andExpect(jsonPath("$.text", is(news.getText())))
                .andExpect(jsonPath("$.commentList", hasSize(0)));

        verify(newsService, times(1)).getNewsByID(Mockito.anyLong());
    }

    @Test
    public void getNewsByID_notFound() throws Exception {
        given(newsService.getNewsByID(Mockito.anyLong())).willThrow(new NoSuchNewsException("There is no news"));

        mockMvc.perform(get("/api/v1/news/{id}", 4))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(newsService, times(1)).getNewsByID(Mockito.anyLong());
    }

    @Test
    public void getAllCommentByIDNews_success() throws Exception {
        news.getCommentList().add(comment);
        news.getCommentList().add(comment2);

        given(newsService.findAllCommentsByIdNews(Mockito.anyLong(), Mockito.any(Pageable.class))).willReturn(news.getCommentList());

        mockMvc.perform(get("/api/v1/news/{idNews}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(comment))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idComment", is(1)))
                .andExpect(jsonPath("$[0].text", is(comment.getText())))
                .andExpect(jsonPath("$[0].idUser", is(comment.getIdUser())))
                .andExpect(jsonPath("$[1].idComment", is(2)))
                .andExpect(jsonPath("$[1].text", is(comment2.getText())))
                .andExpect(jsonPath("$[1].idUser", is(comment2.getIdUser())));

        verify(newsService, times(1)).findAllCommentsByIdNews(Mockito.anyLong(), Mockito.any(Pageable.class));
    }

    @Test
    public void getAllCommentByIDNews_notFound() throws Exception {
        news.getCommentList().add(comment);
        news.getCommentList().add(comment2);

        given(newsService.findAllCommentsByIdNews(Mockito.anyLong(), Mockito.any(Pageable.class))).willThrow(new NoSuchCommentException("News has no comments"));

        mockMvc.perform(get("/api/v1/news/{idNews}/comments", 1))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(newsService, times(1)).findAllCommentsByIdNews(Mockito.anyLong(), Mockito.any(Pageable.class));
    }

    @Test
    public void getCommentByIDWithIDNews_success() throws Exception {
        news.getCommentList().add(comment);

        given(newsService.getCommentByIDWithIDNews(Mockito.anyLong(), Mockito.anyLong())).willReturn(comment);

        mockMvc.perform(get("/api/v1/news/{idNews}/comments/{idComment}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(comment))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idComment", is(1)))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.idUser", is(comment.getIdUser())));

        verify(newsService, times(1)).getCommentByIDWithIDNews(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void getCommentByIDWithIDNews_notFound() throws Exception {
        news.getCommentList().add(comment);

        given(newsService.getCommentByIDWithIDNews(Mockito.anyLong(), Mockito.anyLong())).willThrow(new NoSuchCommentException("No comments"));

        mockMvc.perform(get("/api/v1/news/{idNews}/comments/{idComment}", 1, 4))
                .andExpect(status().isNotFound());

        verify(newsService, times(1)).getCommentByIDWithIDNews(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void saveNews_success() throws Exception {
        given(newsService.saveNews(Mockito.any(NewsDto.class))).willReturn(news);

        mockMvc.perform(post("/api/v1/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(news))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idNews", is(1)))
                .andExpect(jsonPath("$.title", is(news.getTitle())))
                .andExpect(jsonPath("$.text", is(news.getText())))
                .andExpect(jsonPath("$.commentList", hasSize(0)));

        verify(newsService, times(1)).saveNews(Mockito.any(NewsDto.class));
    }

    @Test
    public void saveCommentsByIDNews_success() throws Exception {
        news.getCommentList().add(comment);

        given(newsService.getNewsByID(Mockito.anyLong())).willReturn(news);
        given(commentService.saveComment(Mockito.any(CommentDto.class))).willReturn(comment);

        mockMvc.perform(post("/api/v1/news/{idNews}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(comment))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idComment", is(1)))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.idUser", is(comment.getIdUser())));

        verify(newsService, times(1)).getNewsByID(Mockito.anyLong());
        verify(commentService, times(1)).saveComment(Mockito.any(CommentDto.class));
    }

    @Test
    public void updateNews_success() throws Exception {
        given(newsService.saveNews(Mockito.any(NewsDto.class))).willReturn(news);

        mockMvc.perform(put("/api/v1/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(news))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idNews", is(1)))
                .andExpect(jsonPath("$.title", is(news.getTitle())))
                .andExpect(jsonPath("$.text", is(news.getText())))
                .andExpect(jsonPath("$.commentList", hasSize(0)));

        verify(newsService, times(1)).saveNews(Mockito.any(NewsDto.class));
    }

    @Test
    public void updateCommentByIDNews_success() throws Exception {
        news.getCommentList().add(comment);

        given(newsService.getNewsByID(Mockito.anyLong())).willReturn(news);
        given(commentService.updateComment(Mockito.any(CommentDto.class), Mockito.any(CommentDto.class))).willReturn(comment);

        mockMvc.perform(put("/api/v1/news/{idNews}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(comment))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idComment", is(1)))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.idUser", is(comment.getIdUser())));

        verify(newsService, times(1)).getNewsByID(Mockito.anyLong());
        verify(commentService, times(1)).updateComment(Mockito.any(CommentDto.class), Mockito.any(CommentDto.class));
    }

    @Test
    public void deleteNewsByID_success() throws Exception {
        when(newsService.deleteNews(Mockito.anyLong())).thenReturn("News is deleted");

        mockMvc.perform(delete("/api/v1/news/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uri", is(containsString("/api/v1/news/2"))))
                .andExpect(jsonPath("$.info", is(containsString("delete"))));

        verify(newsService, times(1)).deleteNews(Mockito.anyLong());
    }

    @Test
    public void deleteNewsByID_notFound() throws Exception {
        when(newsService.deleteNews(Mockito.anyLong())).thenThrow(new NoSuchNewsException("No news"));

        mockMvc.perform(delete("/api/v1/news/{id}", 3))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.uri", is("/api/v1/news/3")))
                .andExpect(jsonPath("$.info", is("No news")));

        verify(newsService, times(1)).deleteNews(Mockito.anyLong());
    }

    @Test
    public void deleteCommentByIDWithIDNews_success() throws Exception {
        CommentDto comment3 = new CommentDto(3L, LocalDateTime.now(), "Comment3", "3", 1L);
        news.getCommentList().add(comment3);

        when(newsService.getNewsByID(Mockito.anyLong())).thenReturn(news);
        when(commentService.deleteComment(Mockito.anyLong())).thenReturn("deleted");

        mockMvc.perform(delete("/api/v1/news/{idNews}/comments/{idComment}", 1, 3))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uri", is("/api/v1/news/1/comments/3")))
                .andExpect(jsonPath("$.info", is(containsString("deleted"))));

        verify(commentService, times(1)).deleteComment(Mockito.anyLong());
    }

    @Test
    public void deleteCommentByIDWithIDNews_notFound() throws Exception {
        when(newsService.getNewsByID(Mockito.anyLong())).thenReturn(news);
        when(commentService.deleteComment(Mockito.anyLong())).thenThrow(new NoSuchCommentException("There is no comment with ID 3 in this news"));

        mockMvc.perform(delete("/api/v1/news/{idNews}/comments/{idComment}", 1, 3))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.uri", is("/api/v1/news/1/comments/3")))
                .andExpect(jsonPath("$.info", is(containsString("There is no comment with ID 3 in this news"))));
    }
}