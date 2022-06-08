package by.brel.newsmanagement.mapper;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.entity.Comment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperComment {

    @Autowired
    private ModelMapper modelMapper;

    public CommentDto convertCommentToCommentDto(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }

    public Comment convertCommentDtoToComment(CommentDto commentDto) {
        return modelMapper.map(commentDto, Comment.class);
    }
}
