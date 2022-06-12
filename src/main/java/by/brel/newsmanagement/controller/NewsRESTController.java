package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.dto.NewsDto;
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
    public List<NewsDto> getAllNews(@PageableDefault(sort = "idNews", direction = Sort.Direction.ASC) Pageable pageable) {
        return newsService.getAllNewsPaginated(pageable);
    }

    @GetMapping("/news/{idNews}")
    public NewsDto getNewsByID(@PathVariable long idNews) {
        return newsService.getNewsByID(idNews);
    }

    @GetMapping("/news/{idNews}/comments")
    public List<CommentDto> getAllCommentByIDNews(@PageableDefault(sort = "idComment", direction = Sort.Direction.ASC) Pageable pageable,
                                                  @PathVariable long idNews
    ) {
        List<CommentDto> commentDtoList = newsService.findAllCommentsByIdNews(idNews, pageable);

        return commentDtoList;
    }

    @GetMapping("/news/{idNews}/comments/{idComment}")
    public CommentDto getCommentByIDWithIDNews(@PathVariable long idNews, @PathVariable long idComment) {
        CommentDto commentDto = newsService.getCommentByIDWithIDNews(idNews, idComment);

        return commentDto;
    }

    @PostMapping("/news")
    public NewsDto saveNews(@RequestBody NewsDto newsDto) {
        return newsService.saveNews(newsDto);
    }

    @PostMapping("/news/{idNews}/comments")
    public CommentDto saveCommentsByIDNews(@PathVariable long idNews, @RequestBody CommentDto commentDto) {
        NewsDto newsDto = newsService.getNewsByID(idNews);

        commentDto.setIdNews(newsDto.getIdNews());

        commentService.saveComment(commentDto);

        return commentDto;
    }

    @PutMapping("/news")
    public NewsDto updateNews(@RequestBody NewsDto newsDto) {
        return newsService.saveNews(newsDto);
    }

    @PutMapping("/news/{idNews}/comments")
    public CommentDto updateCommentByIDNews(@PathVariable long idNews, @RequestBody CommentDto commentDto) {
        NewsDto newsDto = newsService.getNewsByID(idNews);

        CommentDto oldCommentDto = newsDto.getCommentList().stream()
                .filter(oldComment2 -> oldComment2.getIdComment().equals(commentDto.getIdComment()))
                .findFirst()
                .orElse(null);

        return commentService.updateComment(commentDto, oldCommentDto);
    }

    @DeleteMapping("/news/{idNews}")
    public String deleteNewsByID(@PathVariable long idNews) {
        String response = newsService.deleteNews(idNews);

        return response;
    }

    @DeleteMapping("/news/{idNews}/comments/{idComment}")
    public String deleteCommentByIDWithIDNews(@PathVariable long idNews, @PathVariable long idComment) {
        String response = commentService.deleteComment(idComment);

        return response;
    }
}
