package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.entity.News;
import by.brel.newsmanagement.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public List<News> searchNews(@RequestParam(value = "keyword", required = false) String keyWord) {
        return searchService.searchNews(keyWord);
    }
}
