package com.unforgettable.securitypart.service;

import com.unforgettable.securitypart.entity.UserEntity;
import com.unforgettable.securitypart.enums.UserRole;
import com.unforgettable.securitypart.exception.UserAlreadyExistsException;
import com.unforgettable.securitypart.model.SecurityUser;
import com.unforgettable.securitypart.model.request.AuthenticationRequest;
import com.unforgettable.securitypart.model.response.AuthenticationResponse;
import com.unforgettable.securitypart.model.request.RegistrationRequest;
import com.unforgettable.securitypart.repository.ApplicationUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtService jwtService,
                                 UserDetailsService userDetailsService,
                                 ApplicationUserRepository applicationUserRepository,
                                 PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        String refreshToken = header.substring(7);
        String username = jwtService.extractUsername(refreshToken);

        UserDetails user = userDetailsService.loadUserByUsername(username);
        String accessToken = jwtService.generateAccessToken(user);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse register(RegistrationRequest request) {
        String username = request.getUsername();
        Boolean isEducator = request.getIsEducator();
        UserRole userRole;

        if (applicationUserRepository.existsByUsername(username))
            throw new UserAlreadyExistsException("User with username = " + username + " has already existed");

        if (isEducator)
            userRole = UserRole.EDUCATOR;
        else
            userRole = UserRole.STUDENT;

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .build();

        applicationUserRepository.save(user);
        SecurityUser securityUser = SecurityUser.parseEntityUser(user);

        String accessToken = jwtService.generateAccessToken(securityUser);
        String refreshToken = jwtService.generateRefreshToken(securityUser);

        return new AuthenticationResponse(accessToken, refreshToken);
    }
}
