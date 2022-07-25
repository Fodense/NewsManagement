package by.brel.newsmanagement.service;

import by.brel.newsmanagement.dto.NewsDto;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface SearchService {

    List<NewsDto> searchNews(Specification specification);
}
