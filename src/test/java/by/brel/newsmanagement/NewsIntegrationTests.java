package by.brel.newsmanagement;

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
class NewsIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NewsRESTController newsRESTController;

    @Test
    public void testGetAllNews() throws Exception {
        mockMvc.perform(get("/api/v1/news"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idNews", is(1)))
                .andExpect(jsonPath("$[0].dateCreatedNews", is(containsString("2022"))))
                .andExpect(jsonPath("$[0].title", is(containsString(anyString()))))
                .andExpect(jsonPath("$[0].text", is(containsString(anyString()))))
                .andExpect(jsonPath("$[0].commentList", hasSize(10)))
                .andExpect(jsonPath("$[1].idNews", is(2)))
                .andExpect(jsonPath("$[1].dateCreatedNews", is(containsString("2022"))))
                .andExpect(jsonPath("$[1].title", is(containsString(anyString()))))
                .andExpect(jsonPath("$[1].text", is(containsString(anyString()))))
                .andExpect(jsonPath("$[1].commentList", hasSize(10)))
                .andExpect(jsonPath("$[9].idNews", is(10)));
    }

    @Test
    public void testGetSingleNewsByID_success() throws Exception {
        mockMvc.perform(get("/api/v1/news/{idNews}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idNews", is(1)))
                .andExpect(jsonPath("$.dateCreatedNews", is(containsString(anyString()))))
                .andExpect(jsonPath("$.title", is(containsString(anyString()))))
                .andExpect(jsonPath("$.text", is(containsString(anyString()))))
                .andExpect(jsonPath("$.commentList", hasSize(10)));
    }

    @Test
    public void testGetSingleNewsByID_notFound() throws Exception {
        mockMvc.perform(get("/api/v1/news/{idNews}", 22))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.uri", is(containsString(anyString()))))
                .andExpect(jsonPath("$.info", is(containsString(anyString()))));
    }

    @Test
    public void testGetAllCommentByIDNews_success() throws Exception {
        mockMvc.perform(get("/api/v1/news/{idNews}/comments", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].idComment", is(1)))
                .andExpect(jsonPath("$[0].dateCreatedComment", is(containsString(anyString()))))
                .andExpect(jsonPath("$[0].text", is(containsString(anyString()))))
                .andExpect(jsonPath("$[0].idUser", is(containsString(anyString()))));
    }

    @Test
    public void testGetAllCommentByIDNews_notFound() throws Exception {
        mockMvc.perform(get("/api/v1/news/{idNews}/comments", 23))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.uri", is(containsString(anyString()))))
                .andExpect(jsonPath("$.info", is(containsString(anyString()))));
    }

    @Test
    public void testGetCommentByIDWithIDNews_success() throws Exception {
        mockMvc.perform(get("/api/v1/news/{idNews}/comments/{idComment}", 1, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idComment", is(1)))
                .andExpect(jsonPath("$.dateCreatedComment", is(containsString(anyString()))))
                .andExpect(jsonPath("$.text", is(containsString(anyString()))))
                .andExpect(jsonPath("$.idUser", is(containsString(anyString()))));
    }

    @Test
    public void testGetCommentByIDWithIDNews_notFound() throws Exception {
        mockMvc.perform(get("/api/v1/news/{idNews}/comments/{idComment}", 1, 100))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.uri", is(containsString(anyString()))))
                .andExpect(jsonPath("$.info", is(containsString(anyString()))));
    }

    @Test
    public void testSaveNews_success() throws Exception {
        NewsDto newsDto = new NewsDto(21L, LocalDateTime.now(), "Test", "Test", new ArrayList<>());

        mockMvc.perform(post("/api/v1/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(newsDto))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idNews", is(21)))
                .andExpect(jsonPath("$.title", is("Test")));
    }

    @Test
    public void testSaveNews_badRequest() throws Exception {
        NewsDto newsDto = new NewsDto(21L, LocalDateTime.now(), "Test", "Test", new ArrayList<>());

        mockMvc.perform(post("/api/v1/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(newsDto.toString())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.uri", is(containsString(anyString()))))
                .andExpect(jsonPath("$.info", is(containsString(anyString()))));
    }

    @Test
    public void testSaveCommentsByIDNews_success() throws Exception {
        CommentDto commentDto = new CommentDto(201L, LocalDateTime.now(), "Comment", "1", 1L);

        mockMvc.perform(post("/api/v1/news/{idNews}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idComment", is(201)))
                .andExpect(jsonPath("$.dateCreatedComment", is(containsString(anyString()))))
                .andExpect(jsonPath("$.text", is(containsString(anyString()))))
                .andExpect(jsonPath("$.idUser", is(containsString(anyString()))));
    }

    @Test
    public void testSaveCommentsByIDNews_badRequest() throws Exception {
        CommentDto commentDto = new CommentDto(201L, LocalDateTime.now(), "Comment", "1", 1L);

        mockMvc.perform(post("/api/v1/news/{idNews}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(commentDto.toString())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.uri", is(containsString(anyString()))))
                .andExpect(jsonPath("$.info", is(containsString(anyString()))));
    }

    @Test
    public void testSaveCommentsByIDNews_notFound() throws Exception {
        CommentDto commentDto = new CommentDto(201L, LocalDateTime.now(), "Comment", "1", 1L);

        mockMvc.perform(post("/api/v1/news/{idNews}/comments", 25)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.uri", is(containsString(anyString()))))
                .andExpect(jsonPath("$.info", is(containsString(anyString()))));
    }

    @Test
    public void testUpdateNews_success() throws Exception {
        NewsDto newsDto = new NewsDto(21L, LocalDateTime.now(), "Test21", "Test21", new ArrayList<>());

        mockMvc.perform(put("/api/v1/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(newsDto))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idNews", is(21)))
                .andExpect(jsonPath("$.title", is("Test21")));
    }

    @Test
    public void testUpdateNews_badRequest() throws Exception {
        NewsDto newsDto = new NewsDto(21L, LocalDateTime.now(), "Test21", "Test21", new ArrayList<>());

        mockMvc.perform(put("/api/v1/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(newsDto.toString())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.uri", is(containsString(anyString()))))
                .andExpect(jsonPath("$.info", is(containsString(anyString()))));
    }

    @Test
    public void testUpdateCommentByIDNews_success() throws Exception {
        CommentDto commentDto = new CommentDto(1L, LocalDateTime.now(), "Comment", "1", 1L);

        mockMvc.perform(put("/api/v1/news/{idNews}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idComment", is(1)))
                .andExpect(jsonPath("$.dateCreatedComment", is(containsString(anyString()))))
                .andExpect(jsonPath("$.text", is(containsString(anyString()))))
                .andExpect(jsonPath("$.idUser", is(containsString(anyString()))));
    }

    @Test
    public void testUpdateCommentByIDNews_badRequest() throws Exception {
        CommentDto commentDto = new CommentDto(201L, LocalDateTime.now(), "Comment", "1", 1L);

        mockMvc.perform(put("/api/v1/news/{idNews}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(commentDto.toString())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.uri", is(containsString(anyString()))))
                .andExpect(jsonPath("$.info", is(containsString(anyString()))));
    }

    @Test
    public void testUpdateCommentByIDNews_notFound() throws Exception {
        CommentDto commentDto = new CommentDto(201L, LocalDateTime.now(), "Comment", "1", 1L);

        mockMvc.perform(put("/api/v1/news/{idNews}/comments", 26)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.uri", is(containsString(anyString()))))
                .andExpect(jsonPath("$.info", is(containsString(anyString()))));
    }

    @Test
    public void testDeleteNewsByID_success() throws Exception {
        mockMvc.perform(delete("/api/v1/news/{idNews}", 21))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uri", is(containsString(anyString()))))
                .andExpect(jsonPath("$.info", is(containsString(anyString()))));
    }

    @Test
    public void testDeleteNewsByID_notFound() throws Exception {
        mockMvc.perform(delete("/api/v1/news/{idNews}", 21))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.uri", is(containsString(anyString()))))
                .andExpect(jsonPath("$.info", is(containsString(anyString()))));
    }

    @Test
    public void testDeleteCommentByIDWithIDNews_success() throws Exception {
        mockMvc.perform(delete("/api/v1/news/{idNews}/comments/{idComment}", 1, 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uri", is(containsString(anyString()))))
                .andExpect(jsonPath("$.info", is(containsString(anyString()))));
    }

    @Test
    public void testDeleteCommentByIDWithIDNews_notFound() throws Exception {
        mockMvc.perform(delete("/api/v1/news/{idNews}/comments/{idComment}", 1, 22))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.uri", is(containsString(anyString()))))
                .andExpect(jsonPath("$.info", is(containsString(anyString()))));
    }
}
