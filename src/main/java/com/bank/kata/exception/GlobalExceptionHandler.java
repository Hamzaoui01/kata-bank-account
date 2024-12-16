package com.bank.kata.exception;

import com.bank.kata.dto.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseBody
    public ApiError handleAccountNotFoundException(AccountNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage());
        return ApiError.create(ex.getMessage(),HttpStatus.NOT_FOUND,request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiError handleAccountNotFoundException(MethodArgumentNotValidException ex, WebRequest request) {
        log.error(ex.getMessage());
        ApiError apiError = ApiError.create(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
        List<String> errorMessages = ex.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        apiError.setMessage(String.join(", ", errorMessages));
        return apiError;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseBody
    public ApiError handleInsufficientBalanceException(InsufficientBalanceException ex, WebRequest request) {
        log.error(ex.getMessage());
        return ApiError.create(ex.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY,request);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OperationsNotFoundException.class)
    @ResponseBody
    public ApiError handleOperationsNotFoundException(OperationsNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage());
        return ApiError.create(ex.getMessage(),HttpStatus.NOT_FOUND,request);
    }

}

