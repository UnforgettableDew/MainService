package com.unforgettable.securitypart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EducatorDTO {
    private String firstname;

    private String lastname;

    private String email;

    private Date birthday;

    @JsonProperty(value = "telegram_contact")
    private String telegramContact;
}
