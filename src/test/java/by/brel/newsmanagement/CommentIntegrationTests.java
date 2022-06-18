package by.brel.newsmanagement;

import by.brel.newsmanagement.controller.CommentRESTController;
import by.brel.newsmanagement.controller.NewsRESTController;
import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.dto.NewsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.yml")
class CommentIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRESTController commentRESTController;

    @Test
    public void testGetAllComments() throws Exception {
        mockMvc.perform(get("/api/v1/comments"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idComment", is(1)))
                .andExpect(jsonPath("$[0].text", is(containsString("Lorem"))))
                .andExpect(jsonPath("$[0].idUser", is(containsString("1"))))
                .andExpect(jsonPath("$[1].idComment", is(2)))
                .andExpect(jsonPath("$[1].text", is(containsString("Aenean"))))
                .andExpect(jsonPath("$[1].idUser", is(containsString("1"))));
    }

    @Test
    public void testGetCommentByID_success() throws Exception {
        mockMvc.perform(get("/api/v1/comments/{idComment}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idComment", is(1)))
                .andExpect(jsonPath("$.text", is(containsString("Lorem"))))
                .andExpect(jsonPath("$.idUser", is(containsString("1"))));
    }

    @Test
    public void testGetCommentByID_notFound() throws Exception {
        mockMvc.perform(get("/api/v1/comments/{idComment}", 300))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.uri", is(containsString("/api/v1/comments/300"))))
                .andExpect(jsonPath("$.info", is(containsString("There is no comment with ID 300"))));
    }

    @Test
    public void testSaveComment_badRequest() throws Exception {
        CommentDto commentDto = new CommentDto(201L, LocalDateTime.now(), "Comment", "1", 1L);

        mockMvc.perform(post("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(commentDto.toString())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.uri", is(containsString("/api/v1/comments"))))
                .andExpect(jsonPath("$.info", is(containsString("JSON parse error"))));
    }

    @Test
    public void testUpdateComment_badRequest() throws Exception {
        CommentDto commentDto = new CommentDto(201L, LocalDateTime.now(), "Comment201", "1", 1L);

        mockMvc.perform(put("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(commentDto.toString())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.uri", is(containsString("/api/v1/comments"))))
                .andExpect(jsonPath("$.info", is(containsString("JSON parse error"))));
    }

    @Test
    public void testDeleteCommentByID_success() throws Exception {
        mockMvc.perform(delete("/api/v1/comments/{idComment}", 200))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uri", is(containsString("/api/v1/comments/200"))))
                .andExpect(jsonPath("$.info", is(containsString("Comment with id 200 is deleted"))));
    }

    @Test
    public void testDeleteCommentByID_notFound() throws Exception {
        mockMvc.perform(delete("/api/v1/comments/{idComment}", 201))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.uri", is(containsString("/api/v1/comments/201"))))
                .andExpect(jsonPath("$.info", is(containsString("There is no comment with ID 201"))));
    }
}
