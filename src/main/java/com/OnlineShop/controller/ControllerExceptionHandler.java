package com.OnlineShop.controller;

import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.ErrorListResponse;
import com.OnlineShop.exception.ErrorResponse;
import com.OnlineShop.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler
{
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(NotFoundException exception)
    {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(AlreadyExistsException exception)
    {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorListResponse> handleException(MethodArgumentNotValidException exception)
    {
        // create ErrorResponse
        ErrorListResponse errorListResponse = new ErrorListResponse();

        List<ObjectError> errorList = exception.getAllErrors();


        List<String> errorMessageList = new ArrayList<>();

        for (ObjectError objError : errorList)
        {
            errorMessageList.add(objError.getDefaultMessage());
//            errorListResponse.AddMessage(objError.getDefaultMessage());
        }

        errorListResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorListResponse.setMessage(errorMessageList);
        errorListResponse.setTimeStamp(System.currentTimeMillis());

        // return ResponseEntity
        return new ResponseEntity<>(errorListResponse, HttpStatus.BAD_REQUEST);
    }
}
