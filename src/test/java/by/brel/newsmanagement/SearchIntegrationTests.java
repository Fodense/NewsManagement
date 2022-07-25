package by.brel.newsmanagement;

import by.brel.newsmanagement.controller.SearchRESTController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                .andExpect(jsonPath("$[0].title", is(containsString("Lorem ipsum"))))
                .andExpect(jsonPath("$[0].text", is(containsString("Lorem"))));
    }

    @Test
    public void testSearchNews_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/news/search")
                        .param("keyword", "222222")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.uri", is(containsString("/api/v1/news/search"))))
                .andExpect(jsonPath("$.info", is(containsString("No matches were found"))));
    }
}
