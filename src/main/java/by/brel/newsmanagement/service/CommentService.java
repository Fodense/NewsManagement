package by.brel.newsmanagement.service;

import by.brel.newsmanagement.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAllComment();

    Comment getCommentByID(long id);

    Comment saveComment(Comment comment);

    Comment updateComment(Comment comment, Comment oldComment);

    void deleteComment(long id);
}
