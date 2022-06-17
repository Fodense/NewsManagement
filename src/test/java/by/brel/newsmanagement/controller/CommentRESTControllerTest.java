package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.dto.NewsDto;
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
@WebMvcTest(CommentRESTController.class)
public class CommentRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    private NewsDto news;
    private NewsDto news2;
    private CommentDto comment;
    private CommentDto comment2;

    @Before
    public void init() {
        news = new NewsDto(1L, getNowDateTime(), "Test", "Test", new ArrayList<>());
        news2 = new NewsDto(2L, getNowDateTime(), "Test2", "Test2", new ArrayList<>());
        comment = new CommentDto(1L, getNowDateTime(), "Comment", "1", news.getIdNews());
        comment2 = new CommentDto(2L, getNowDateTime(), "Comment2", "2", news.getIdNews());
    }

    @Test
    public void getAllComment() throws Exception {
        List<CommentDto> commentDtoList = List.of(comment, comment2);

        given(commentService.getAllCommentPaginated(any())).willReturn(commentDtoList);

        mockMvc.perform(get("/api/v1/comments"))
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

        verify(commentService, times(1)).getAllCommentPaginated(any());
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void getCommentByID() throws Exception {
        given(commentService.getCommentByID(Mockito.anyLong())).willReturn(comment);

        mockMvc.perform(get("/api/v1/comments/{id}", comment.getIdComment()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idComment", is(comment.getIdComment().intValue())))
                .andExpect(jsonPath("$.dateCreatedComment", is(comment.getDateCreatedComment().toString())))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.idUser", is(comment.getIdUser())));

        verify(commentService, times(1)).getCommentByID(Mockito.anyLong());
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void saveComment() throws Exception {
        given(commentService.saveComment(Mockito.any(CommentDto.class))).willReturn(comment);

        mockMvc.perform(post("/api/v1/comments")
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

        verify(commentService, times(1)).saveComment(Mockito.any(CommentDto.class));
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void updateComment() throws Exception {
        given(commentService.saveComment(Mockito.any(CommentDto.class))).willReturn(comment);

        mockMvc.perform(put("/api/v1/comments")
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

        verify(commentService, times(1)).saveComment(Mockito.any(CommentDto.class));
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void deleteComment() throws Exception {
        when(commentService.deleteComment(Mockito.anyLong())).thenReturn("Comment is deleted");

        MvcResult requestResult = mockMvc.perform(delete("/api/v1/comments/{idComment}", comment.getIdComment()))
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