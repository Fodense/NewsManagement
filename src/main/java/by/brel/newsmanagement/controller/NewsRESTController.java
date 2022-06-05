package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.entity.Comment;
import by.brel.newsmanagement.entity.News;
import by.brel.newsmanagement.service.CommentService;
import by.brel.newsmanagement.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NewsRESTController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/news")
    public List<News> getAllNews(@PageableDefault(sort = "idNews", direction = Sort.Direction.ASC) Pageable pageable) {
        return newsService.getAllNewsPaginated(pageable);
    }

    @GetMapping("/news/{idNews}")
    public News getNewsByID(@PathVariable long idNews) {
        return newsService.getNewsByID(idNews);
    }

    @GetMapping("/news/{idNews}/comments")
    public List<Comment> getAllCommentByIDNews(@PageableDefault(sort = "idComment", direction = Sort.Direction.ASC) Pageable pageable,
                                               @PathVariable long idNews
    ) {
        List<Comment> commentList = newsService.findAllCommentsByIdNews(idNews, pageable);

        return commentList;
    }

    @GetMapping("/news/{idNews}/comments/{idComment}")
    public Comment getCommentByID(@PathVariable long idNews, @PathVariable long idComment) {
        Comment comment = newsService.getCommentByIDWithIDNews(idNews, idComment);

        return comment;
    }

    @PostMapping("/news")
    public News saveNews(@RequestBody News news) {
        return newsService.saveNews(news);
    }

    @PostMapping("/news/{idNews}/comments")
    public Comment saveCommentsByIDNews(@PathVariable long idNews, @RequestBody Comment comment) {
        News news = newsService.getNewsByID(idNews);
        news.getCommentList().add(comment);

        newsService.saveNews(news);

        return comment;
    }

    @PutMapping("/news")
    public News updateNews(@RequestBody News news) {
        return newsService.saveNews(news);
    }

    @PutMapping("/news/{idNews}/comments")
    public Comment updateCommentByIDNews(@PathVariable long idNews, @RequestBody Comment comment) {
        News news = newsService.getNewsByID(idNews);

        Comment oldComment = news.getCommentList().stream()
                .filter(oldComment2 -> oldComment2.getIdComment().equals(comment.getIdComment()))
                .findFirst()
                .orElse(null);

        return commentService.updateComment(comment, oldComment);
    }

    @DeleteMapping("/news/{idNews}")
    public void deleteNewsByID(@PathVariable long idNews) {
        newsService.deleteNews(idNews);
    }

    @DeleteMapping("/news/{idNews}/comments/{idComment}")
    public void deleteCommentByIDWithIDNews(@PathVariable long idNews, @PathVariable long idComment) {
        commentService.deleteComment(idComment);
    }
}
