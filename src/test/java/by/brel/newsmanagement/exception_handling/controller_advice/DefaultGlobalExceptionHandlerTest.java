package by.brel.newsmanagement.exception_handling.controller_advice;

import by.brel.newsmanagement.controller.CommentRESTController;
import by.brel.newsmanagement.exception_handling.exception.NoSuchCommentException;
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
@WebMvcTest(CommentRESTController.class)
public class DefaultGlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentRESTController commentRESTController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentRESTController)
                .setControllerAdvice(new CommentGlobalExceptionHandler())
                .build();
    }

    @Test
    public void defaultHandleExceptionNotFoundCommentByID() throws Exception {
        when(commentRESTController.getCommentByID(anyLong())).thenThrow(new NoSuchCommentException("Unexpected Exception"));

        mockMvc.perform(get("/api/v1/comments/{id}", "String"))
                .andExpect(status().isBadRequest());
    }
}