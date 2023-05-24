package com.unforgettable.securitypart.security;

import com.unforgettable.securitypart.model.request.BaseFilterExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class ApplicationAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        BaseFilterExceptionResponse.generateFilterExceptionResponse(
                request,
                response,
                UNAUTHORIZED,
                "Wrong username or password"
        );
    }
}
