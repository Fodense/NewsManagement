package by.brel.newsmanagement.service.impl;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.entity.Comment;
import by.brel.newsmanagement.exception_handling.exception.NoSuchCommentException;
import by.brel.newsmanagement.mapper.CommentMapper;
import by.brel.newsmanagement.repository.CommentRepository;
import by.brel.newsmanagement.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    /**
     * Method return all comment from DB
     * and convert comment to commentDto
     *
     * @param pageable object for pagination, if parameters were specified in url
     * @see Comment
     * @see CommentDto
     * @throws NoSuchCommentException if comments not found in DB
     * @return List with commentDto json
     */
    @Override
    @Cacheable(cacheNames = "comments")
    public List<CommentDto> getAllCommentPaginated(Pageable pageable) {
        List<Comment> commentList = commentRepository.findAll(pageable).toList();

        if (commentList.isEmpty()) {
            throw new NoSuchCommentException("No comments");
        }

        return commentList.stream()
                .map(comment -> commentMapper.convertCommentToCommentDto(comment))
                .collect(Collectors.toList());
    }

    /**
     * Method returns a single comment
     * and convert comment to commentDto
     *
     * @param id parameter for search comment with id
     * @see Comment
     * @see CommentDto
     * @throws NoSuchCommentException if comments not found in DB
     * @return comment json
     */
    @Override
    public CommentDto getCommentByID(long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NoSuchCommentException("There is no comment with ID " + id));

        return commentMapper.convertCommentToCommentDto(comment);
    }

    /**
     * Method for save comment in DB
     * and convert comment to commentDto
     *
     * @param commentDto object, which comes in the body of the request
     * @return commentDto json, if save is successful
     */
    @Override
    @CacheEvict(cacheNames = "comments", allEntries = true)
    public CommentDto saveComment(CommentDto commentDto) {
        //setting date for new comment
        commentDto.setDateCreatedComment(LocalDateTime.now());

        Comment comment = commentRepository.save(commentMapper.convertCommentDtoToComment(commentDto));

        commentDto.setIdComment(comment.getIdComment());

        return commentDto;
    }

    /**
     * Method for update comment in DB
     * and convert comment to commentDto
     *
     * @param commentDto
     * @param oldCommentDto
     * @return commentDto json
     */
    @Override
    @CacheEvict(cacheNames = "comments", allEntries = true)
    public CommentDto updateComment(CommentDto commentDto, CommentDto oldCommentDto) {
        commentDto.setDateCreatedComment(oldCommentDto.getDateCreatedComment());
        commentDto.setIdNews(oldCommentDto.getIdNews());

        commentRepository.save(commentMapper.convertCommentDtoToComment(commentDto));

        return commentDto;
    }

    /**
     * Method for delete comment by id
     *
     * @param id parameter, which is used when deleting a commentDto
     * @return info on operation
     */
    @Override
    @CacheEvict(cacheNames = "comments", allEntries = true)
    public String deleteComment(long id) {
        getCommentByID(id);

        commentRepository.deleteById(id);

        return "Comment with id " + id + " is deleted";
    }
}
