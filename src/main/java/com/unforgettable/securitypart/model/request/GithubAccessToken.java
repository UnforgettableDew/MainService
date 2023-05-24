package com.unforgettable.securitypart.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubAccessToken {
    @JsonProperty("github_access_token")
    private String githubAccessToken;
}
