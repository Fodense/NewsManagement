package by.brel.newsmanagement.exception_handling.controller_advice;

import by.brel.newsmanagement.exception_handling.DefaultResponseData;
import by.brel.newsmanagement.exception_handling.exception.NoSuchDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class SearchGlobalExceptionHandler {

    @ExceptionHandler(NoSuchDataException.class)
    public ResponseEntity<DefaultResponseData> handleException(NoSuchDataException noSuchDataException, HttpServletRequest request) {
        DefaultResponseData data = new DefaultResponseData();
        data.setUri(request.getRequestURI());
        data.setInfo(noSuchDataException.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }
}
