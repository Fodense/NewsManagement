package by.brel.newsmanagement.service.impl;

import by.brel.newsmanagement.entity.Comment;
import by.brel.newsmanagement.entity.news.News;
import by.brel.newsmanagement.repository.CommentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    private CommentRepository commentRepository;

    private LocalDateTime localDateTime;
    private News news;
    private Comment comment;

    private List<Comment> commentList;

    @Before
    public void init() {
        commentRepository = mock(CommentRepository.class);

        localDateTime = LocalDateTime.now();
        news = new News(1L, localDateTime, "Test", "Test", new ArrayList<>());
        comment = new Comment(1L, localDateTime, "Comment", "1", news);

        commentList = new ArrayList<>();
        commentList.add(comment);
    }

    @Test
    public void getAllComment() {
        when(commentRepository.findAll()).thenReturn(commentList);

        List<Comment> commentList2 = commentRepository.findAll();

        assertEquals(1, commentList2.size());

        verify(commentRepository, times(1)).findAll();
    }

    @Test
    public void getCommentByID() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(comment));

        Comment comment = commentRepository.findById(anyLong()).orElse(null);

        assertEquals("1", comment.getIdComment().toString());

        verify(commentRepository, times(1)).findById(anyLong());
    }

    @Test
    public void saveComment() {
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentRepository.save(comment);

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void updateComment() {
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentRepository.save(comment);

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void deleteComment() {
        commentRepository.deleteById(anyLong());

        verify(commentRepository,times(1)).deleteById(anyLong());
    }
}