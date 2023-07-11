package com.example.leonproject.exception;

import com.example.leonproject.config.MyLocaleResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private final MyLocaleResolver myLocaleResolver;

    public GlobalExceptionHandler(MessageSource messageSource, MyLocaleResolver myLocaleResolver) {
        this.messageSource = messageSource;
        this.myLocaleResolver = myLocaleResolver;
    }

    @ExceptionHandler(AccountExistedException.class)
    public ResponseEntity<ErrorResponse> handleAccountExistedException(HttpServletRequest httpServletRequest) {

        Locale locale = myLocaleResolver.resolveLocale(httpServletRequest);
        String errorMessage = messageSource.getMessage("error.usernameExists", null, locale);
        ErrorResponse errorResponse = new ErrorResponse(-1, errorMessage);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(InputValidationException.class)
    public ResponseEntity<ErrorResponse> handleInputValidationException(InputValidationException ex) {

        String errorMessage = ex.getErrorMessage();

        ErrorResponse errorResponse = new ErrorResponse(-1, errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(PunchClockFailException.class)
    public ResponseEntity<ErrorResponse> handlePunchClockFailException(PunchClockFailException ex) {

        String errorMessage = ex.getErrorMessage();

        ErrorResponse errorResponse = new ErrorResponse(-1, errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
