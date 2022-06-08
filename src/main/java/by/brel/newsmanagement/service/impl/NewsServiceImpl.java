package by.brel.newsmanagement.service.impl;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.dto.NewsDto;
import by.brel.newsmanagement.entity.Comment;
import by.brel.newsmanagement.entity.News;
import by.brel.newsmanagement.mapper.MapperComment;
import by.brel.newsmanagement.mapper.MapperNews;
import by.brel.newsmanagement.repository.CommentRepository;
import by.brel.newsmanagement.repository.NewsRepository;
import by.brel.newsmanagement.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
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

        return newsList.toList().stream()
                .map(news -> mapperNews.convertNewsToNewsDto(news))
                .collect(Collectors.toList());
    }

    @Override
    public NewsDto getNewsByID(long id) {
        News news = newsRepository.findById(id).orElse(null);

        return mapperNews.convertNewsToNewsDto(news);
    }

    @Override
    public List<CommentDto> findAllCommentsByIdNews(long idNews, Pageable pageable) {
        News news = newsRepository.findById(idNews).orElse(null);

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

        return commentDto;
    }

    @Override
    public NewsDto saveNews(NewsDto newsDto) {
        List<CommentDto> commentDtoList = newsDto.getCommentList();

        if (!commentDtoList.isEmpty()) {
            commentDtoList.stream()
                    .filter(comment -> comment.getDateCreatedComment() == null || comment.getIdNews() == null)
                    .peek(comment -> comment.setIdNews(newsDto.getIdNews()))
                    .forEach(comment -> comment.setDateCreatedComment(new Date()));
        }

        if (newsDto.getDateCreatedNews() == null) {
            newsDto.setDateCreatedNews(new Date());
        }

        News news = newsRepository.save(mapperNews.convertNewsDtoToNews(newsDto));
        newsDto.setIdNews(news.getIdNews());

        return newsDto;
    }

    @Override
    public void deleteNews(long id) {
        newsRepository.deleteById(id);
    }
}
