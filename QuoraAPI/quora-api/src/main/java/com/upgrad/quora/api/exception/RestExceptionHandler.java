package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
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
}
