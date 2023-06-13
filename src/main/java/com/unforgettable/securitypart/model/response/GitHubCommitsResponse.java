package com.unforgettable.securitypart.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubCommitsResponse {
    @JsonProperty("repository_url")
    private String repositoryUrl;
    private String task;
    @JsonProperty("commit_list")
    private List<Object> commitList;
}
