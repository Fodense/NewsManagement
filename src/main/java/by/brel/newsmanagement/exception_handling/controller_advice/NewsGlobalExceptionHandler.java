package by.brel.newsmanagement.exception_handling.controller_advice;

import by.brel.newsmanagement.exception_handling.DefaultResponseData;
import by.brel.newsmanagement.exception_handling.exception.NoSuchNewsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class NewsGlobalExceptionHandler {

    @ExceptionHandler(NoSuchNewsException.class)
    public ResponseEntity<DefaultResponseData> handleException(NoSuchNewsException noSuchNewsException, HttpServletRequest request) {
        DefaultResponseData data = new DefaultResponseData();
        data.setUri(request.getRequestURI());
        data.setInfo(noSuchNewsException.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultResponseData> handleException(Exception exception, HttpServletRequest request) {
        DefaultResponseData data = new DefaultResponseData();
        data.setUri(request.getRequestURI());
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
