package com.unforgettable.securitypart.security;

import com.unforgettable.securitypart.model.request.BaseFilterExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class ApplicationAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exc) throws IOException {
        BaseFilterExceptionResponse.generateFilterExceptionResponse(
                request,
                response,
                FORBIDDEN,
                exc.getMessage()
        );
    }
}
