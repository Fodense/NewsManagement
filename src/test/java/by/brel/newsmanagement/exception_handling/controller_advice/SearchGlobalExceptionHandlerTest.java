package by.brel.newsmanagement.exception_handling.controller_advice;

import by.brel.newsmanagement.controller.SearchRESTController;
import by.brel.newsmanagement.exception_handling.exception.NoSuchDataException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchRESTController.class)
public class SearchGlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchRESTController searchRESTController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(searchRESTController)
                .setControllerAdvice(new SearchGlobalExceptionHandler())
                .build();
    }

    @Test
    public void searchHandleExceptionNotFoundByKeyWord() throws Exception {
        when(searchRESTController.searchNews(anyString())).thenThrow(new NoSuchDataException("Unexpected Exception"));

        mockMvc.perform(get("/api/v1/news/search")
                        .param("keyword", anyString())
                )
                .andExpect(status().isNotFound());
    }
}