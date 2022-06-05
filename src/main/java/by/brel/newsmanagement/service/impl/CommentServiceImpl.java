package by.brel.newsmanagement.service.impl;

import by.brel.newsmanagement.entity.Comment;
import by.brel.newsmanagement.repository.CommentRepository;
import by.brel.newsmanagement.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getAllComment() {
        return commentRepository.findAll();
    }

    @Override
    public Comment getCommentByID(long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public Comment saveComment(Comment comment) {
        comment.setDateCreatedComment(new Date());

        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Comment comment, Comment oldComment) {
        comment.setDateCreatedComment(oldComment.getDateCreatedComment());
        comment.setNews(oldComment.getNews());

        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }
}
