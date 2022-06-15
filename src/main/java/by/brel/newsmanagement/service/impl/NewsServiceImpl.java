package by.brel.newsmanagement.service.impl;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.dto.NewsDto;
import by.brel.newsmanagement.entity.Comment;
import by.brel.newsmanagement.entity.News;
import by.brel.newsmanagement.exception_handling.exception.NoSuchCommentException;
import by.brel.newsmanagement.exception_handling.exception.NoSuchNewsException;
import by.brel.newsmanagement.mapper.MapperComment;
import by.brel.newsmanagement.mapper.MapperNews;
import by.brel.newsmanagement.repository.CommentRepository;
import by.brel.newsmanagement.repository.NewsRepository;
import by.brel.newsmanagement.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MapperNews mapperNews;

    @Autowired
    private MapperComment mapperComment;

    @Override
    public List<NewsDto> getAllNewsPaginated(Pageable pageable) {
        Page<News> newsList = newsRepository.findAll(pageable);

        if (newsList.isEmpty()) {
            throw new NoSuchNewsException("No news");
        }

        return newsList.toList().stream()
                .map(news -> mapperNews.convertNewsToNewsDto(news))
                .collect(Collectors.toList());
    }

    @Override
    public NewsDto getNewsByID(long id) {
        News news = newsRepository.findById(id).orElseThrow(() -> new NoSuchNewsException("There is no news with ID " + id));

        return mapperNews.convertNewsToNewsDto(news);
    }

    @Override
    public List<CommentDto> findAllCommentsByIdNews(long idNews, Pageable pageable) {
        News news = mapperNews.convertNewsDtoToNews(getNewsByID(idNews));

        if (news.getCommentList().isEmpty()) {
            throw new NoSuchCommentException("News with ID " + idNews + " has no comments");
        }

        return commentRepository.findAllCommentsByNews(news, pageable).stream()
                .map(comment -> mapperComment.convertCommentToCommentDto(comment))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentByIDWithIDNews(long idNews, long idComment) {
        NewsDto news = getNewsByID(idNews);

        List<CommentDto> commentDtoList = news.getCommentList();

        CommentDto commentDto = commentDtoList.stream()
                .filter(comment -> comment.getIdComment() == idComment)
                .findFirst()
                .orElse(null);

        if (commentDto == null) {
            throw new NoSuchNewsException("There is no comment with ID " + idComment);
        }

        return commentDto;
    }

    @Override
    public NewsDto saveNews(NewsDto newsDto) {
        News news = mapperNews.convertNewsDtoToNews(newsDto);

        List<Comment> commentList = news.getCommentList();

        //add information about News to the Comment entry and setting the date
        if (!commentList.isEmpty()) {
            commentList.stream()
                    .filter(comment -> comment.getDateCreatedComment() == null || comment.getNews() == null)
                    .peek(comment -> comment.setNews(news))
                    .forEach(comment -> comment.setDateCreatedComment(LocalDateTime.now()));
        }

        //setting date in new news
        if (news.getDateCreatedNews() == null) {
            news.setDateCreatedNews(LocalDateTime.now());
        }

        News news2 = newsRepository.save(news);

        return mapperNews.convertNewsToNewsDto(news2);
    }

    @Override
    public String deleteNews(long id) {
        newsRepository.deleteById(id);

        return "News with id " + id + " is deleted";
    }
}
