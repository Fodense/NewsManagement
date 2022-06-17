package by.brel.newsmanagement.service;

import by.brel.newsmanagement.dto.CommentDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    List<CommentDto> getAllCommentPaginated(Pageable pageable);

    CommentDto getCommentByID(long id);

    CommentDto saveComment(CommentDto commentDto);

    CommentDto updateComment(CommentDto commentDto, CommentDto oldCommentDto);

    String deleteComment(long id);
}
