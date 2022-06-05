package by.brel.newsmanagement.service.impl;

import by.brel.newsmanagement.entity.Comment;
import by.brel.newsmanagement.entity.News;
import by.brel.newsmanagement.repository.CommentRepository;
import by.brel.newsmanagement.repository.NewsRepository;
import by.brel.newsmanagement.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<News> getAllNewsPaginated(Pageable pageable) {
        Page<News> newsList = newsRepository.findAll(pageable);

        return newsList.toList();
    }

    @Override
    public News getNewsByID(long id) {
        News news = newsRepository.findById(id).orElse(null);

        return news;
    }

    @Override
    public List<Comment> findAllCommentsByIdNews(long idNews, Pageable pageable) {

        News news = newsRepository.findById(idNews).orElse(null);

        return commentRepository.findAllCommentsByNews(news, pageable);
    }

    @Override
    public Comment getCommentByIDWithIDNews(long idNews, long idComment) {
        News news = getNewsByID(idNews);

        List<Comment> commentList = news.getCommentList();

        Comment comment = commentList.stream()
                .filter(comment2 -> comment2.getIdComment() == idComment)
                .findFirst()
                .orElse(null);

        return comment;
    }

    @Override
    public News saveNews(News news) {
        List<Comment> commentList = news.getCommentList();

        if (!commentList.isEmpty()) {
            commentList.stream()
                    .filter(comment -> comment.getDateCreatedComment() == null || comment.getNews() == null)
                    .peek(comment -> comment.setNews(news))
                    .forEach(comment -> comment.setDateCreatedComment(new Date()));
        }

        if (news.getDateCreatedNews() == null) {
            news.setDateCreatedNews(new Date());
        }

        News newsSave = newsRepository.save(news);

        return newsSave;
    }

    @Override
    public void deleteNews(long id) {
        newsRepository.deleteById(id);
    }
}
