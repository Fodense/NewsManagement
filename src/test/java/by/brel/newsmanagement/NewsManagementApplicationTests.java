package by.brel.newsmanagement;

import by.brel.newsmanagement.repository.NewsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
@Sql({"/db/schema.sql", "/db/data.sql"})
class NewsManagementApplicationTests {

    @Autowired
    private NewsRepository newsRepository;

    @Test
    void contextLoads() {
    }

    @Test
    public void testLoadDataForNews() {
        assertEquals(20, newsRepository.findAll().size());
    }

}
