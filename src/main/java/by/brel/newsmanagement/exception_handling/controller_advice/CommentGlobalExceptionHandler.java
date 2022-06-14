package by.brel.newsmanagement.exception_handling.controller_advice;

import by.brel.newsmanagement.exception_handling.CommentIncorrectData;
import by.brel.newsmanagement.exception_handling.exception.NoSuchCommentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommentGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<CommentIncorrectData> handleException(NoSuchCommentException noSuchCommentException) {
        CommentIncorrectData data = new CommentIncorrectData();
        data.setInfo(noSuchCommentException.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CommentIncorrectData> handleException(Exception exception) {
        CommentIncorrectData data = new CommentIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
