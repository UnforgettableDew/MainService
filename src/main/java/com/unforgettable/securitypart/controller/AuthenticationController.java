package com.unforgettable.securitypart.controller;

import com.unforgettable.securitypart.model.request.AuthenticationRequest;
import com.unforgettable.securitypart.model.response.AuthenticationResponse;
import com.unforgettable.securitypart.model.request.RegistrationRequest;
import com.unforgettable.securitypart.model.response.RoleResponse;
import com.unforgettable.securitypart.service.AuthenticationService;
import com.unforgettable.securitypart.service.GitHubAccessTokenProvider;
import com.unforgettable.securitypart.service.GitHubRepositoryCreator;
import com.unforgettable.securitypart.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*",
        allowedHeaders = "*",
        exposedHeaders = "*",
        methods = {GET, POST, PUT, DELETE},
        maxAge = 3600)
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final GitHubAccessTokenProvider gitHubAccessTokenProvider;
    @Autowired
    private GitHubRepositoryCreator repositoryCreator;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService,
                                    JwtService jwtService, GitHubAccessTokenProvider gitHubAccessTokenProvider) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.gitHubAccessTokenProvider = gitHubAccessTokenProvider;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest request){
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refresh(HttpServletRequest request, HttpServletResponse response){
        return new ResponseEntity<>(authenticationService.refreshToken(request, response), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody RegistrationRequest request){
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
    }

    @GetMapping("/user-role")
    public ResponseEntity<RoleResponse> checkUserRole(HttpServletRequest request){
        return new ResponseEntity<>(new RoleResponse(jwtService.getUserRole(request)), HttpStatus.OK);
    }

    @GetMapping("/github/callback")
    public String handleGitHubCallback(@RequestParam("code") String code) {
        // Отримати токен доступу з GitHubOAuthService
        String accessToken = gitHubAccessTokenProvider.getAccessToken(code);
        // Зберегти токен доступу у вашій системі (наприклад, у сеансі користувача)
        // для подальшого використання в запитах до GitHub API
        repositoryCreator.createRepository(accessToken, "new-repository");

        // Перенаправити користувача на домашню сторінку або іншу потрібну сторінку
        return accessToken;
    }
    @GetMapping
    public String hello(){
        return "Hello";
    }
}
