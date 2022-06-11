package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.dto.NewsDto;
import by.brel.newsmanagement.service.CommentService;
import by.brel.newsmanagement.service.NewsService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    NewsDto news = new NewsDto(1L, getNowDateTime(), "Test", "Test", new ArrayList<>());
    NewsDto news2 = new NewsDto(2L, getNowDateTime(), "Test2", "Test2", new ArrayList<>());
    CommentDto comment = new CommentDto(1L, getNowDateTime(), "Comment", "1", news.getIdNews());
    CommentDto comment2 = new CommentDto(2L, getNowDateTime(), "Comment2", "2", news.getIdNews());

    @Test
    public void getAllNews() throws Exception {
        List<NewsDto> newsDtoList = List.of(news, news2);

        given(newsService.getAllNewsPaginated(Mockito.any(PageRequest.class))).willReturn(newsDtoList);

        mockMvc.perform(get("/api/v1/news"))
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

        verify(newsService, times(1)).getAllNewsPaginated(Mockito.any(Pageable.class));
        verifyNoMoreInteractions(newsService);
    }

    @Test
    public void getNewsByID() throws Exception {
        given(newsService.getNewsByID(Mockito.anyLong())).willReturn(news);

        mockMvc.perform(get("/api/v1/news/{id}", news.getIdNews()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idNews", is(news.getIdNews().intValue())))
                .andExpect(jsonPath("$.dateCreatedNews", is(news.getDateCreatedNews().toString())))
                .andExpect(jsonPath("$.title", is(news.getTitle())))
                .andExpect(jsonPath("$.text", is(news.getText())))
                .andExpect(jsonPath("$.commentList", hasSize(0)));

        verify(newsService, times(1)).getNewsByID(Mockito.anyLong());
        verifyNoMoreInteractions(newsService);
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
                .andExpect(jsonPath("$[0].dateCreatedComment", is(comment.getDateCreatedComment().toString())))
                .andExpect(jsonPath("$[0].text", is(comment.getText())))
                .andExpect(jsonPath("$[0].idUser", is(comment.getIdUser())))
                .andExpect(jsonPath("$[1].idComment", is(comment2.getIdComment().intValue())))
                .andExpect(jsonPath("$[1].dateCreatedComment", is(comment2.getDateCreatedComment().toString())))
                .andExpect(jsonPath("$[1].text", is(comment2.getText())))
                .andExpect(jsonPath("$[1].idUser", is(comment2.getIdUser())));

        verify(newsService, times(1)).findAllCommentsByIdNews(Mockito.anyLong(), Mockito.any(Pageable.class));
        verifyNoMoreInteractions(newsService);
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
                .andExpect(jsonPath("$.dateCreatedComment", is(comment.getDateCreatedComment().toString())))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.idUser", is(comment.getIdUser())));

        verify(newsService, times(1)).getCommentByIDWithIDNews(Mockito.anyLong(), Mockito.anyLong());
        verifyNoMoreInteractions(newsService);
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
                .andExpect(jsonPath("$.dateCreatedNews", is(news.getDateCreatedNews().toString())))
                .andExpect(jsonPath("$.title", is(news.getTitle())))
                .andExpect(jsonPath("$.text", is(news.getText())))
                .andExpect(jsonPath("$.commentList", hasSize(0)));

        verify(newsService, times(1)).saveNews(Mockito.any(NewsDto.class));
        verifyNoMoreInteractions(newsService);
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
                .andExpect(jsonPath("$.dateCreatedComment", is(comment.getDateCreatedComment().toString())))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.idUser", is(comment.getIdUser())));

        verify(newsService, times(1)).getNewsByID(Mockito.anyLong());
        verify(commentService, times(1)).saveComment(Mockito.any(CommentDto.class));
        verifyNoMoreInteractions(newsService);
        verifyNoMoreInteractions(commentService);
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
                .andExpect(jsonPath("$.dateCreatedNews", is(news.getDateCreatedNews().toString())))
                .andExpect(jsonPath("$.title", is(news.getTitle())))
                .andExpect(jsonPath("$.text", is(news.getText())))
                .andExpect(jsonPath("$.commentList", hasSize(0)));

        verify(newsService, times(1)).saveNews(Mockito.any(NewsDto.class));
        verifyNoMoreInteractions(newsService);
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
                .andExpect(jsonPath("$.dateCreatedComment", is(comment.getDateCreatedComment().toString())))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.idUser", is(comment.getIdUser())));

        verify(newsService, times(1)).getNewsByID(Mockito.anyLong());
        verify(commentService, times(1)).updateComment(Mockito.any(CommentDto.class), Mockito.any(CommentDto.class));
        verifyNoMoreInteractions(newsService);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void deleteNewsByID() throws Exception {
        when(newsService.deleteNews(Mockito.anyLong())).thenReturn("News is deleted");

        MvcResult requestResult = mockMvc.perform(delete("/api/v1/news/{id}", news.getIdNews()))
                .andExpect(status().isOk())
                .andReturn();

        String result = requestResult.getResponse().getContentAsString();
        assertEquals(result, "News is deleted");

        verify(newsService, times(1)).deleteNews(Mockito.anyLong());
        verifyNoMoreInteractions(newsService);
    }

    @Test
    public void deleteCommentByIDWithIDNews() throws Exception {
        when(commentService.deleteComment(Mockito.anyLong())).thenReturn("Comment is deleted");

        MvcResult requestResult = mockMvc.perform(delete("/api/v1/news/{idNews}/comments/{idComment}", news.getIdNews(), comment.getIdComment()))
                .andExpect(status().isOk())
                .andReturn();

        String result = requestResult.getResponse().getContentAsString();
        assertEquals(result, "Comment is deleted");

        verify(commentService, times(1)).deleteComment(Mockito.anyLong());
        verifyNoMoreInteractions(commentService);
    }

    public LocalDateTime getNowDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(dateFormatter);

        return LocalDateTime.parse(dateTime, dateFormatter);
    }
}