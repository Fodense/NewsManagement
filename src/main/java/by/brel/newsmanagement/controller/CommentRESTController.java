package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.exception_handling.DefaultResponseData;
import by.brel.newsmanagement.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentRESTController {

    @Autowired
    private CommentService commentService;

    /**
     * Method return all commentDto from DB
     *
     * @param pageable object for pagination, if parameters were specified in url, example: /api/v1/comments?page=0&size=10
     * @see CommentDto
     * @return List with commentDto json
     */
    @GetMapping("/comments")
    public List<CommentDto> getAllComment(@PageableDefault(sort = "idComment", direction = Sort.Direction.ASC) Pageable pageable) {
        return commentService.getAllCommentPaginated(pageable);
    }

    /**
     * Method returns a single commentDto
     *
     * @param id parameter for search commentDto with id, example: /api/v1/comments/1
     * @see CommentDto
     * @return commentDto json
     */
    @GetMapping("/comments/{id}")
    public CommentDto getCommentByID(@PathVariable long id) {
        return commentService.getCommentByID(id);
    }

    /**
     * Method for save commentDto in DB
     *
     * @param commentDto object, which comes in the body of the request
     * @see CommentDto
     * @return commentDto json, if save is successful
     */
    @PostMapping("/comments")
    public CommentDto saveComment(@RequestBody CommentDto commentDto) {
        return commentService.saveComment(commentDto);
    }

    /**
     * Method for update commentDto
     *
     * @param commentDto new object, which comes in the body of the request and update existing commentDto
     * @see CommentDto
     * @return commentDto json, if update is successful
     */
    @PutMapping("/comments")
    public CommentDto updateComment(@RequestBody CommentDto commentDto) {
        return commentService.saveComment(commentDto);
    }

    /**
     * Method for delete commentDto in DB
     *
     * @param id parameter, which is used when deleting a commentDto
     * @param request parameter, for get request uri
     * @see CommentDto
     * @see DefaultResponseData
     * @return json with info on operation
     */
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<DefaultResponseData> deleteComment(@PathVariable long id, HttpServletRequest request) {
        String response = commentService.deleteComment(id);

        DefaultResponseData data = new DefaultResponseData();
        data.setUri(request.getRequestURI());
        data.setInfo(response);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
