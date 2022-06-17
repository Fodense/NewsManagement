package by.brel.newsmanagement.service.impl;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.entity.Comment;
import by.brel.newsmanagement.exception_handling.exception.NoSuchCommentException;
import by.brel.newsmanagement.mapper.MapperComment;
import by.brel.newsmanagement.repository.CommentRepository;
import by.brel.newsmanagement.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MapperComment mapperComment;

    @Override
    public List<CommentDto> getAllComment() {
        List<Comment> commentList = commentRepository.findAll();

        if (commentList.isEmpty()) {
            throw new NoSuchCommentException("No comments");
        }

        return commentList.stream()
                .map(comment -> mapperComment.convertCommentToCommentDto(comment))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentByID(long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NoSuchCommentException("There is no comment with ID " + id));

        return mapperComment.convertCommentToCommentDto(comment);
    }

    @Override
    public CommentDto saveComment(CommentDto commentDto) {
        //setting date for new comment
        commentDto.setDateCreatedComment(LocalDateTime.now());

        Comment comment = commentRepository.save(mapperComment.convertCommentDtoToComment(commentDto));

        commentDto.setIdComment(comment.getIdComment());

        return commentDto;
    }

    @Override
    public CommentDto updateComment(CommentDto commentDto, CommentDto oldCommentDto) {
        commentDto.setDateCreatedComment(oldCommentDto.getDateCreatedComment());
        commentDto.setIdNews(oldCommentDto.getIdNews());

        commentRepository.save(mapperComment.convertCommentDtoToComment(commentDto));

        return commentDto;
    }

    @Override
    public String deleteComment(long id) {
        getCommentByID(id);

        commentRepository.deleteById(id);

        return "Comment with id " + id + " is deleted";
    }
}
