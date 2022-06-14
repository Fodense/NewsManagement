package by.brel.newsmanagement.exception_handling.controller_advice;

import by.brel.newsmanagement.controller.NewsRESTController;
import by.brel.newsmanagement.exception_handling.exception.NoSuchNewsException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(NewsRESTController.class)
public class NewsGlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsRESTController newsRESTController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(newsRESTController)
                .setControllerAdvice(new NewsGlobalExceptionHandler())
                .build();
    }

    @Test
    public void newsHandleExceptionNotFoundNewsByID() throws Exception {
        when(newsRESTController.getNewsByID(anyLong())).thenThrow(new NoSuchNewsException("Unexpected Exception"));

        mockMvc.perform(get("/api/v1/news/{id}", anyLong()))
                .andExpect(status().isNotFound());
    }
}