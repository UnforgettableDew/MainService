package com.unforgettable.securitypart.feign;

import com.unforgettable.securitypart.model.request.RepositoryImportRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "github", url = "http://localhost:8081/api/v1")
@Component
public interface GithubFeign {
    @GetMapping("/github/user/{username}/repo/{repositoryName}/all-commits")
    List<Object> getAllCommits(@PathVariable String username,
                               @PathVariable String repositoryName);

    @GetMapping("/github/user/{username}/repo/{repositoryName}/files")
    List<Object> getFiles(@PathVariable String username,
                          @PathVariable String repositoryName);

    @PostMapping("/github/repo/create")
    Object createRepo(@RequestParam("access_token") String accessToken,
                      @RequestBody Object request);

    @PutMapping("/github/repo/import")
    Object importRepo(@RequestParam("access_token") String accessToken,
                      @RequestBody RepositoryImportRequest request);

    @GetMapping("/auth/github/callback")
    Map<String, String> handleGitHubCallback(@RequestParam("code") String code);
}
