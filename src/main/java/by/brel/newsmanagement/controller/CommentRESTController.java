package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.exception_handling.DefaultResponseData;
import by.brel.newsmanagement.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/comments")
    public List<CommentDto> getAllComment() {
        return commentService.getAllComment();
    }

    @GetMapping("/comments/{id}")
    public CommentDto getCommentByID(@PathVariable long id) {
        return commentService.getCommentByID(id);
    }

    @PostMapping("/comments")
    public CommentDto saveComment(@RequestBody CommentDto commentDto) {
        return commentService.saveComment(commentDto);
    }

    @PutMapping("/comments")
    public CommentDto updateComment(@RequestBody CommentDto commentDto) {
        return commentService.saveComment(commentDto);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<DefaultResponseData> deleteComment(@PathVariable long id, HttpServletRequest request) {
        String response = commentService.deleteComment(id);

        DefaultResponseData data = new DefaultResponseData();
        data.setUri(request.getRequestURI());
        data.setInfo(response);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
