package by.brel.newsmanagement.service;

import by.brel.newsmanagement.entity.Comment;
import by.brel.newsmanagement.entity.News;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {

    List<News> getAllNewsPaginated(Pageable pageable);

    News getNewsByID(long id);

    List<Comment> findAllCommentsByIdNews(long idNews, Pageable pageable);

    Comment getCommentByIDWithIDNews(long idNews, long idComment);

    News saveNews(News news);

    void deleteNews(long id);
}
