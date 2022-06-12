package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String deleteComment(@PathVariable long id) {
        String response = commentService.deleteComment(id);

        return response;
    }

}
