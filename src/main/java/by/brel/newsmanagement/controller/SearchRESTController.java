package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.dto.NewsDto;
import by.brel.newsmanagement.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
public class SearchRESTController {

    @Autowired
    private SearchService searchService;

    /**
     * Method for search info in table News by title and text fields
     *
     * @param keyWord string for target search
     * @see NewsDto
     * @return result search
     */
    @GetMapping("/search")
    public List<NewsDto> searchNews(@RequestParam(value = "keyword", required = false) String keyWord) {
        return searchService.searchNews(keyWord);
    }
}
