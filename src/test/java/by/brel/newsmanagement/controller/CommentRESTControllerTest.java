package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.dto.NewsDto;
import by.brel.newsmanagement.exception_handling.exception.NoSuchCommentException;
import by.brel.newsmanagement.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentRESTController.class)
public class CommentRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRESTController commentRESTController;

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
        comment = new CommentDto(1L, LocalDateTime.now(), "Comment", "1", 1L);
        comment2 = new CommentDto(2L, LocalDateTime.now(), "Comment2", "2", 2L);
    }

    @Test
    public void getAllComment_success() throws Exception {
        List<CommentDto> commentDtoList = List.of(comment, comment2);

        given(commentService.getAllCommentPaginated(any())).willReturn(commentDtoList);

        mockMvc.perform(get("/api/v1/comments"))
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

        verify(commentService, times(1)).getAllCommentPaginated(any());
    }

    @Test
    public void getCommentByID_success() throws Exception {
        given(commentService.getCommentByID(2)).willReturn(comment2);

        mockMvc.perform(get("/api/v1/comments/{id}", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idComment", is(2)))
                .andExpect(jsonPath("$.text", is(comment2.getText())))
                .andExpect(jsonPath("$.idUser", is(comment2.getIdUser())));

        verify(commentService, times(1)).getCommentByID(2);
    }

    @Test
    public void getCommentByID_notFound() throws Exception {
        given(commentService.getCommentByID(Mockito.anyLong())).willThrow(new NoSuchCommentException("No comment"));

        mockMvc.perform(get("/api/v1/comments/{id}", 1))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.uri", is("/api/v1/comments/1")))
                .andExpect(jsonPath("$.info", is(containsString("No comment"))));

        verify(commentService, times(1)).getCommentByID(Mockito.anyLong());
    }

    @Test
    public void deleteComment_success() throws Exception {
        when(commentService.deleteComment(Mockito.anyLong())).thenReturn("Deleted");

        mockMvc.perform(delete("/api/v1/comments/{idComment}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uri", is("/api/v1/comments/1")))
                .andExpect(jsonPath("$.info", is(containsString("Deleted"))));

        verify(commentService, times(1)).deleteComment(Mockito.anyLong());
    }

    @Test
    public void deleteComment_notFound() throws Exception {
        when(commentService.deleteComment(Mockito.anyLong())).thenThrow(new NoSuchCommentException("No comment"));

        mockMvc.perform(delete("/api/v1/comments/{idComment}", 1))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.uri", is("/api/v1/comments/1")))
                .andExpect(jsonPath("$.info", is(containsString("No comment"))));

        verify(commentService, times(1)).deleteComment(Mockito.anyLong());
    }
}