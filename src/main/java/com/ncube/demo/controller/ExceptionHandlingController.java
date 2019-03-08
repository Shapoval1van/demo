package com.ncube.demo.controller;


import com.ncube.demo.controller.dto.ErrorResponse;
import com.ncube.demo.exception.FileUploadException;
import com.ncube.demo.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> fileUploadExceptionHandler(FileUploadException e) {
        return new ResponseEntity<>(new ErrorResponse("Can't upload file"), HttpStatus.NOT_FOUND);
    }
}
