package by.brel.newsmanagement.service.impl;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.dto.NewsDto;
import by.brel.newsmanagement.entity.Comment;
import by.brel.newsmanagement.entity.News;
import by.brel.newsmanagement.exception_handling.exception.NoSuchCommentException;
import by.brel.newsmanagement.exception_handling.exception.NoSuchNewsException;
import by.brel.newsmanagement.mapper.CommentMapper;
import by.brel.newsmanagement.mapper.NewsMapper;
import by.brel.newsmanagement.repository.CommentRepository;
import by.brel.newsmanagement.repository.NewsRepository;
import by.brel.newsmanagement.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    private NewsMapper newsMapper;

    @Autowired
    private CommentMapper commentMapper;

    /**
     * Method return all News with it Comments from DB or throw Exception
     * and convert news to newsDto
     * This method caches the result
     *
     * @param pageable object for pagination, if parameters were specified in url
     * @see News
     * @see NewsDto
     * @throws NoSuchNewsException if news not found in DB
     * @return List with newsDto json
     */
    @Override
    @Cacheable("news")
    public List<NewsDto> getAllNewsPaginated(Pageable pageable) {
        Page<News> newsList = newsRepository.findAll(pageable);

        if (newsList.isEmpty()) {
            throw new NoSuchNewsException("No news");
        }

        return newsList.toList().stream()
                .map(news -> newsMapper.convertNewsToNewsDto(news))
                .collect(Collectors.toList());
    }

    /**
     * Method returns newsDto
     * and convert news to newsDto
     *
     * @param id parameter for search news with id
     * @see News
     * @see NewsDto
     * @throws NoSuchNewsException if news not found in DB
     * @return newsDto json
     */
    @Override
    public NewsDto getNewsByID(long id) {
        News news = newsRepository.findById(id).orElseThrow(() -> new NoSuchNewsException("There is no news with ID " + id));

        return newsMapper.convertNewsToNewsDto(news);
    }

    /**
     * Method return all comments from DB
     * and convert news, comment to newsDto, commentDto
     * This method caches the result
     *
     * @param idNews parameter for search news with id, example: /api/v1/news/1
     * @param pageable object for pagination, if parameters were specified in url
     * @see News
     * @see NewsDto
     * @see Comment
     * @see CommentDto
     * @throws NoSuchCommentException if comments not found in DB
     * @return List with commentsDto json
     */
    @Override
    @Cacheable(cacheNames = "comments")
    public List<CommentDto> findAllCommentsByIdNews(long idNews, Pageable pageable) {
        News news = newsMapper.convertNewsDtoToNews(getNewsByID(idNews));

        if (news.getCommentList().isEmpty()) {
            throw new NoSuchCommentException("News with ID " + idNews + " has no comments");
        }

        return commentRepository.findAllCommentsByNews(news, pageable).stream()
                .map(comment -> commentMapper.convertCommentToCommentDto(comment))
                .collect(Collectors.toList());
    }

    /**
     * Method returns a single commentDto
     *
     * @param idNews parameter for search news with id, example: /api/v1/news/1
     * @param idComment parameter for search comment with id, example: /api/v1//news/1/comments/1
     * @see News
     * @see NewsDto
     * @see Comment
     * @see CommentDto
     * @throws NoSuchNewsException if news not found in DB
     * @throws NoSuchCommentException if comments not found in DB
     * @return commentDto json
     */
    @Override
    public CommentDto getCommentByIDWithIDNews(long idNews, long idComment) {
        NewsDto news = getNewsByID(idNews);

        List<CommentDto> commentDtoList = news.getCommentList();

        //search comment id with idComment
        CommentDto commentDto = commentDtoList.stream()
                .filter(comment -> comment.getIdComment() == idComment)
                .findFirst()
                .orElseThrow(() -> new NoSuchCommentException("There is no comment with ID " + idComment));

        return commentDto;
    }

    /**
     * Method for save news in DB
     * and convert news to newsDto
     * This method resets the cache
     *
     * @param newsDto object, which comes in the body of the request
     * @see News
     * @see NewsDto
     * @return news json, if save is successful
     */
    @Override
    @CacheEvict(cacheNames = "news", allEntries = true)
    public NewsDto saveNews(NewsDto newsDto) {
        News news = newsMapper.convertNewsDtoToNews(newsDto);

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

        return newsMapper.convertNewsToNewsDto(news2);
    }

    /**
     * Method search news by id and delete it in DB
     * This method resets the cache
     *
     * @param id parameter, which is used when deleting a newsDto
     * @throws NoSuchNewsException if news not found in DB
     * @return info on operation
     */
    @Override
    @CacheEvict(cacheNames = "news", allEntries = true)
    public String deleteNews(long id) {
        getNewsByID(id);

        newsRepository.deleteById(id);

        return "News with id " + id + " is deleted";
    }
}
