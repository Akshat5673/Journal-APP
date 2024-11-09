package com.nineleaps.journalApp.Exceptions;

import com.nineleaps.journalApp.Dtos.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundException (ResourceNotFoundException e){
        String message= e.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiResponse> alreadyExistsException (AlreadyExistsException e){
        String message= e.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,false);
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }
}
