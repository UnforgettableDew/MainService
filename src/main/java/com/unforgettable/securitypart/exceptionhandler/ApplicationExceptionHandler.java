package com.unforgettable.securitypart.exceptionhandler;

import com.unforgettable.securitypart.exception.*;
import com.unforgettable.securitypart.model.response.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException exception,
                                                                 HttpServletRequest request) {
        HttpStatus httpStatus = NOT_FOUND;
        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .httpStatus(httpStatus)
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, httpStatus);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<Object> handleUsernameAlreadyExistsException(UserAlreadyExistsException exception,
                                                                       HttpServletRequest request) {
        HttpStatus httpStatus = BAD_REQUEST;

        ExceptionResponse response = new ExceptionResponse(
                exception.getMessage(),
                httpStatus,
                Timestamp.valueOf(LocalDateTime.now()),
                request.getRequestURI());

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {CourseDoesntBelongEducatorException.class})
    public ResponseEntity<Object> handleCourseDoesntBelongEducatorException(CourseDoesntBelongEducatorException exception,
                                                                       HttpServletRequest request) {
        HttpStatus httpStatus = BAD_REQUEST;

        ExceptionResponse response = new ExceptionResponse(
                exception.getMessage(),
                httpStatus,
                Timestamp.valueOf(LocalDateTime.now()),
                request.getRequestURI());

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {NoSuchStudentOnCourseException.class})
    public ResponseEntity<Object> handleNoSuchStudentOnCourseException(NoSuchStudentOnCourseException exception,
                                                                       HttpServletRequest request) {
        HttpStatus httpStatus = BAD_REQUEST;

        ExceptionResponse response = new ExceptionResponse(
                exception.getMessage(),
                httpStatus,
                Timestamp.valueOf(LocalDateTime.now()),
                request.getRequestURI());

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {TaskDoesntBelongCourseException.class})
    public ResponseEntity<Object> handleTaskDoesntBelongCourseException(TaskDoesntBelongCourseException exception,
                                                                       HttpServletRequest request) {
        HttpStatus httpStatus = BAD_REQUEST;

        ExceptionResponse response = new ExceptionResponse(
                exception.getMessage(),
                httpStatus,
                Timestamp.valueOf(LocalDateTime.now()),
                request.getRequestURI());

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {NoPassedTaskException.class})
    public ResponseEntity<Object> handleNoLaboratoryWorkException(NoPassedTaskException exception,
                                                                  HttpServletRequest request) {
        HttpStatus httpStatus = BAD_REQUEST;

        ExceptionResponse response = new ExceptionResponse(
                exception.getMessage(),
                httpStatus,
                Timestamp.valueOf(LocalDateTime.now()),
                request.getRequestURI());

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }
}
