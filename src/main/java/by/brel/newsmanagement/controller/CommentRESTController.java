package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.entity.Comment;
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
    public List<Comment> getAllComment() {
        return commentService.getAllComment();
    }

    @GetMapping("/comments/{id}")
    public Comment getCommentByID(@PathVariable long id) {
        return commentService.getCommentByID(id);
    }

    @PostMapping("/comments")
    public Comment saveComment(@RequestBody Comment comment) {
        return commentService.saveComment(comment);
    }

    @PutMapping("/comments")
    public Comment updateComment(@RequestBody Comment comment) {
        return commentService.saveComment(comment);
    }

    @DeleteMapping("/comments/{id}")
    public void deleteComment(@PathVariable long id) {
        commentService.deleteComment(id);
    }

}
