package by.brel.newsmanagement.service;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.dto.NewsDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {

    List<NewsDto> getAllNewsPaginated(Pageable pageable);

    NewsDto getNewsByID(long id);

    List<CommentDto> findAllCommentsByIdNews(long idNews, Pageable pageable);

    CommentDto getCommentByIDWithIDNews(long idNews, long idComment);

    NewsDto saveNews(NewsDto newsDto);

    void deleteNews(long id);
}
