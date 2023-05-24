package com.unforgettable.securitypart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class GitHubRepositoryCreator {
    @Autowired
    private RestTemplate restTemplate;

    public boolean createRepository(String accessToken, String repositoryName) {
//        String apiUrl = "https://api.github.com/user/repos";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("name", repositoryName);
//
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
//
//        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
//        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
//            System.out.println("Repository created successfully");
//        } else {
//            System.out.println("Failed to create repository");

        String url = "https://api.github.com/user";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            HttpStatusCode statusCode = responseEntity.getStatusCode();

            if (statusCode.is2xxSuccessful()) {
                // Токен має права доступу
                return true;
            } else {
                // Токен не має прав доступу
                return false;
            }
        } catch (HttpClientErrorException e) {
            // Помилка запиту (наприклад, невірний токен або недостатні права)
            return false;
        }
    }
}
