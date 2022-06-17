package by.brel.newsmanagement;

import by.brel.newsmanagement.controller.CommentRESTController;
import by.brel.newsmanagement.controller.SearchRESTController;
import by.brel.newsmanagement.dto.CommentDto;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.yml")
class SearchIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SearchRESTController searchRESTController;

    @Test
    public void testSearchNews_success() throws Exception {
        mockMvc.perform(get("/api/v1/news/search")
                        .param("keyword", "Lorem")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idNews", is(1)))
                .andExpect(jsonPath("$[0].dateCreatedNews", is(containsString(anyString()))))
                .andExpect(jsonPath("$[0].title", is(containsString(anyString()))))
                .andExpect(jsonPath("$[0].text", is(containsString(anyString()))));
    }

    @Test
    public void testSearchNews_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/news/search")
                        .param("keyword", "2")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.uri", is(containsString(anyString()))))
                .andExpect(jsonPath("$.info", is(containsString(anyString()))));
    }
}
