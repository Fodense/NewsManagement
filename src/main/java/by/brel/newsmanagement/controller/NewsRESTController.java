package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.dto.NewsDto;
import by.brel.newsmanagement.entity.News;
import by.brel.newsmanagement.exception_handling.DefaultResponseData;
import by.brel.newsmanagement.exception_handling.exception.NoSuchCommentException;
import by.brel.newsmanagement.service.CommentService;
import by.brel.newsmanagement.service.NewsService;
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
    public ResponseEntity<DefaultResponseData> deleteNewsByID(@PathVariable long idNews, HttpServletRequest request) {
        String response = newsService.deleteNews(idNews);

        DefaultResponseData data = new DefaultResponseData();
        data.setUri(request.getRequestURI());
        data.setInfo(response);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("/news/{idNews}/comments/{idComment}")
    public ResponseEntity<DefaultResponseData> deleteCommentByIDWithIDNews(@PathVariable long idNews, @PathVariable long idComment, HttpServletRequest request) {
        NewsDto newsDto = getNewsByID(idNews);

        // check for comments on this post
        newsDto.getCommentList().stream()
                .filter(commentDto -> commentDto.getIdComment() == idComment)
                .findFirst()
                .orElseThrow(() -> new NoSuchCommentException("There is no comment with ID " + idComment + " in this news"));

        String response = commentService.deleteComment(idComment);

        DefaultResponseData data = new DefaultResponseData();
        data.setUri(request.getRequestURI());
        data.setInfo(response);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
