package com.unforgettable.securitypart.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResponse {
    @JsonProperty("successful_action")
    private Boolean successfulAction;
}
