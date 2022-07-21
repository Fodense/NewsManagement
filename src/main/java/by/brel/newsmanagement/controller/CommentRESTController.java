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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentRESTController {

    @Autowired
    private CommentService commentService;

    /**
     * Method return all commentDto from DB
     *
     * @param pageable object for pagination, if parameters were specified in url
     * @see CommentDto
     * @return List with commentDto json
     */
    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> getAllComment(@PageableDefault(sort = "idComment", direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(commentService.getAllCommentPaginated(pageable), HttpStatus.OK);
    }

    /**
     * Method returns a single commentDto
     *
     * @param id parameter for search commentDto with id, example: /api/v1/comments/1
     * @see CommentDto
     * @return commentDto json
     */
    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentDto> getCommentByID(@PathVariable @NotNull long id) {
        return new ResponseEntity<>(commentService.getCommentByID(id), HttpStatus.OK);
    }

    /**
     * Method for save commentDto in DB
     *
     * @param commentDto object, which comes in the body of the request
     * @see CommentDto
     * @return commentDto json, if save is successful
     */
    @PostMapping("/comments")
    public ResponseEntity<CommentDto> saveComment(@Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.saveComment(commentDto), HttpStatus.OK);
    }

    /**
     * Method for update commentDto
     *
     * @param commentDto new object, which comes in the body of the request and update existing commentDto
     * @see CommentDto
     * @return commentDto json, if update is successful
     */
    @PutMapping("/comments")
    public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.saveComment(commentDto), HttpStatus.OK);
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
    public ResponseEntity<DefaultResponseData> deleteComment(@PathVariable @NotNull long id, HttpServletRequest request) {
        String response = commentService.deleteComment(id);

        DefaultResponseData data = new DefaultResponseData();
        data.setUri(request.getRequestURI());
        data.setInfo(response);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
