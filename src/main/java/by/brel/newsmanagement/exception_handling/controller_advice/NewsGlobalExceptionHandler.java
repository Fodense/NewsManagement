package by.brel.newsmanagement.exception_handling.controller_advice;

import by.brel.newsmanagement.exception_handling.NewsIncorrectData;
import by.brel.newsmanagement.exception_handling.exception.NoSuchNewsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NewsGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<NewsIncorrectData> handleException(NoSuchNewsException noSuchNewsException) {
        NewsIncorrectData data = new NewsIncorrectData();
        data.setInfo(noSuchNewsException.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<NewsIncorrectData> handleException(Exception exception) {
        NewsIncorrectData data = new NewsIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
