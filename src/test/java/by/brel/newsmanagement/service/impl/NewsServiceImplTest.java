package by.brel.newsmanagement.service.impl;

import by.brel.newsmanagement.entity.Comment;
import by.brel.newsmanagement.entity.News;
import by.brel.newsmanagement.repository.CommentRepository;
import by.brel.newsmanagement.repository.NewsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewsServiceImplTest {

    private NewsRepository newsRepository;
    private CommentRepository commentRepository;

    private LocalDateTime localDateTime;
    private News news;
    private Comment comment;

    private List<News> newsList;
    private List<Comment> commentList;

    @Before
    public void init() {
        newsRepository = mock(NewsRepository.class);
        commentRepository = mock(CommentRepository.class);

        localDateTime = LocalDateTime.now();
        news = new News(1L, localDateTime, "Test", "Test", new ArrayList<>());
        comment = new Comment(1L, localDateTime, "Comment", "1", news);

        newsList = new ArrayList<>();
        newsList.add(news);

        commentList = new ArrayList<>();
        commentList.add(comment);
    }

    @Test
    public void getAllNewsPaginated() {
        when(newsRepository.findAll((Sort) any())).thenReturn(newsList);

        List<News> newsList = newsRepository.findAll((Sort) any());

        assertEquals(1, newsList.size());

        verify(newsRepository, times(1)).findAll((Sort) any());
    }

    @Test
    public void getNewsByID() {
        when(newsRepository.findById(news.getIdNews())).thenReturn(Optional.of(news));

        News news2 = newsRepository.findById(news.getIdNews()).orElse(null);

        assertEquals("1", news2.getIdNews().toString());
        assertEquals(localDateTime, news2.getDateCreatedNews());
        assertEquals("Test", news2.getTitle());
        assertEquals("Test", news2.getText());
        assertNotNull(news2.getCommentList());

        verify(newsRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    public void findAllCommentsByIdNews() {
        when(commentRepository.findAllCommentsByNews(any(), any())).thenReturn(commentList);

        List<Comment> commentList2 = commentRepository.findAllCommentsByNews(any(), any());

        assertEquals(1, commentList2.size());

        verify(commentRepository, times(1)).findAllCommentsByNews(any(), any());
    }

    @Test
    public void getCommentByIDWithIDNews() {
        when(newsRepository.findById(anyLong())).thenReturn(Optional.of(news));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        Comment comment2 = commentRepository.findById(anyLong()).orElse(null);

        assertEquals("1", comment2.getIdComment().toString());
        assertEquals(localDateTime, comment2.getDateCreatedComment());
        assertEquals("Comment", comment2.getText());
        assertEquals("1", comment2.getIdUser());
        assertEquals(news, comment2.getNews());

        verify(commentRepository, times(1)).findById(anyLong());
    }

    @Test
    public void saveNews() {
        newsRepository.save(news);

        verify(newsRepository, times(1)).save(any(News.class));
    }

    @Test
    public void deleteNews() {
        newsRepository.deleteById(anyLong());

        verify(newsRepository, times(1)).deleteById(anyLong());
    }
}