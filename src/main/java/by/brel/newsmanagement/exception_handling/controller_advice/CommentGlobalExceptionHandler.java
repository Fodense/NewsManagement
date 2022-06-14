package by.brel.newsmanagement.exception_handling.controller_advice;

import by.brel.newsmanagement.exception_handling.DefaultResponseData;
import by.brel.newsmanagement.exception_handling.exception.NoSuchCommentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CommentGlobalExceptionHandler {

    @ExceptionHandler(NoSuchCommentException.class)
    public ResponseEntity<DefaultResponseData> handleException(NoSuchCommentException noSuchCommentException, HttpServletRequest request) {
        DefaultResponseData data = new DefaultResponseData();
        data.setUri(request.getRequestURI());
        data.setInfo(noSuchCommentException.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }
}
