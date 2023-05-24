package com.unforgettable.securitypart.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unforgettable.securitypart.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    @JsonProperty("role")
    private String role;
}
