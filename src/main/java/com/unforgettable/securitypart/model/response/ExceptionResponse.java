package com.unforgettable.securitypart.model.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ExceptionResponse {
    private String message;
    private HttpStatus httpStatus;
    private Timestamp timestamp;
    private String path;
}
