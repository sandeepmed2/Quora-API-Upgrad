package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signupRestrictedException(SignUpRestrictedException ex, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.CONFLICT //Http status code 409 for duplicate resource or resource already exists
        );
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException ex, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.NOT_FOUND //Http status code 404 for invalid username or password
        );
    }

    @ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> signoutRestrictedException(SignOutRestrictedException ex, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.UNAUTHORIZED //Http status code 401 when the provided access token is not available in database
        );
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException ex, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.FORBIDDEN //Http status code 403 when supplied access token is invalid
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException ex, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.NOT_FOUND //Http status code 404 when given user id does not exist in database
        );
    }

    @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<ErrorResponse> invalidQuestionException(InvalidQuestionException ex, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.NOT_FOUND //Http status code 404 when given question does not exist in database
        );
    }

    @ExceptionHandler(AnswerNotFoundException.class)
    public ResponseEntity<ErrorResponse> answerNotFoundException(AnswerNotFoundException ex, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.NOT_FOUND //Http status code 404 when given answer does not exist in database
        );
    }
}
