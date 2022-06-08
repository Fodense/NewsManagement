package by.brel.newsmanagement.service;

import by.brel.newsmanagement.dto.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getAllComment();

    CommentDto getCommentByID(long id);

    CommentDto saveComment(CommentDto commentDto);

    CommentDto updateComment(CommentDto commentDto, CommentDto oldCommentDto);

    void deleteComment(long id);
}
