package by.brel.newsmanagement.controller;

import by.brel.newsmanagement.dto.CommentDto;
import by.brel.newsmanagement.dto.NewsDto;
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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NewsRESTController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private CommentService commentService;

    /**
     * Method return all newsDto with it CommentsDto from DB
     *
     * @param pageable object for pagination, if parameters were specified in url
     * @see NewsDto
     * @return List with newsDto json
     */
    @GetMapping("/news")
    public ResponseEntity<List<NewsDto>> getAllNews(@PageableDefault(sort = "idNews", direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(newsService.getAllNewsPaginated(pageable), HttpStatus.OK);
    }

    /**
     * Method returns newsDto
     *
     * @param idNews parameter for search news with id, example: /api/v1/news/1
     * @see NewsDto
     * @return newsDto json
     */
    @GetMapping("/news/{idNews}")
    public ResponseEntity<NewsDto> getNewsByID(@PathVariable @NotNull long idNews) {
        return new ResponseEntity<>(newsService.getNewsByID(idNews), HttpStatus.OK);
    }

    /**
     * Method return all commentsDto from DB
     *
     * @param pageable object for pagination, if parameters were specified in url
     * @param idNews parameter for search newsDto with id, example: /api/v1/news/1
     * @see NewsDto
     * @see CommentDto
     * @return List with commentsDto json
     */
    @GetMapping("/news/{idNews}/comments")
    public ResponseEntity<List<CommentDto>> getAllCommentByIDNews(@PageableDefault(sort = "idComment", direction = Sort.Direction.ASC) Pageable pageable,
                                                  @PathVariable @NotNull long idNews
    ) {
        List<CommentDto> commentDtoList = newsService.findAllCommentsByIdNews(idNews, pageable);

        return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
    }

    /**
     * Method returns a single commentDto
     *
     * @param idNews parameter for search newsDto with id, example: /api/v1/news/1
     * @param idComment parameter for search commentDto with id, example: /api/v1//news/1/comments/1
     * @see NewsDto
     * @see CommentDto
     * @return commentDto json
     */
    @GetMapping("/news/{idNews}/comments/{idComment}")
    public ResponseEntity<CommentDto> getCommentByIDWithIDNews(@PathVariable @NotNull long idNews, @PathVariable @NotNull long idComment) {
        CommentDto commentDto = newsService.getCommentByIDWithIDNews(idNews, idComment);

        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    /**
     * Method for save newsDto in DB
     *
     * @param newsDto object, which comes in the body of the request
     * @see NewsDto
     * @return newsDto json, if save is successful
     */
    @PostMapping("/news")
    public ResponseEntity<NewsDto> saveNews(@Valid @RequestBody NewsDto newsDto) {
        NewsDto newsDto2 = newsService.saveNews(newsDto);

        return new ResponseEntity<>(newsDto2, HttpStatus.CREATED);
    }

    /**
     * Method for save commentDto in DB for newsDto
     *
     * @param idNews parameter for search newsDto with id, example: /api/v1/news/1
     * @param commentDto object, which comes in the body of the request
     * @see NewsDto
     * @see CommentDto
     * @return commentDto json, if save is successful
     */
    @PostMapping("/news/{idNews}/comments")
    public ResponseEntity<CommentDto> saveCommentsByIDNews(@PathVariable @NotNull long idNews, @Valid @RequestBody CommentDto commentDto) {
        NewsDto newsDto = newsService.getNewsByID(idNews);

        commentDto.setIdNews(newsDto.getIdNews());

        commentService.saveComment(commentDto);

        return new ResponseEntity<>(commentDto, HttpStatus.CREATED);
    }

    /**
     * Method for update newsDto in DB
     *
     * @param newsDto object, which comes in the body of the request
     * @see NewsDto
     * @return newsDto json, if update is successful
     */
    @PutMapping("/news")
    public ResponseEntity<NewsDto> updateNews(@Valid @RequestBody NewsDto newsDto) {
        return new ResponseEntity<>(newsService.saveNews(newsDto), HttpStatus.OK);
    }

    /**
     * Method for update commentDto in DB for news
     *
     * @param idNews parameter for search news with id, example: /api/v1/news/1
     * @param commentDto object, which comes in the body of the request
     * @see NewsDto
     * @see CommentDto
     * @return comment json, if update is successful
     */
    @PutMapping("/news/{idNews}/comments")
    public ResponseEntity<CommentDto> updateCommentByIDNews(@PathVariable @NotNull long idNews, @Valid @RequestBody CommentDto commentDto) {
        NewsDto newsDto = newsService.getNewsByID(idNews);

        CommentDto oldCommentDto = newsDto.getCommentList().stream()
                .filter(oldComment2 -> oldComment2.getIdComment().equals(commentDto.getIdComment()))
                .findFirst()
                .orElse(null);

        return new ResponseEntity<>(commentService.updateComment(commentDto, oldCommentDto), HttpStatus.OK);
    }

    /**
     * Method for delete newsDto in DB
     *
     * @param idNews parameter, which is used when deleting a newsDto
     * @param request parameter, for get request uri
     * @see NewsDto
     * @see DefaultResponseData
     * @return json with info on operation
     */
    @DeleteMapping("/news/{idNews}")
    public ResponseEntity<DefaultResponseData> deleteNewsByID(@PathVariable @NotNull long idNews, HttpServletRequest request) {
        String response = newsService.deleteNews(idNews);

        DefaultResponseData data = new DefaultResponseData();
        data.setUri(request.getRequestURI());
        data.setInfo(response);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * @param idNews parameter, which is used when deleting a newsDto
     * @param idComment parameter, which is used when deleting a commentDto
     * @param request parameter, for get request uri
     * @see NewsDto
     * @see CommentDto
     * @see DefaultResponseData
     * @return json with info on operation
     */
    @DeleteMapping("/news/{idNews}/comments/{idComment}")
    public ResponseEntity<DefaultResponseData> deleteCommentByIDWithIDNews(@PathVariable @NotNull long idNews, @PathVariable @NotNull long idComment, HttpServletRequest request) {
        NewsDto newsDto = newsService.getNewsByID(idNews);

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
