package com.unforgettable.securitypart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unforgettable.securitypart.entity.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TypicalMistakeDTO {
    private Long id;
    private String title;
    private String description;
    @JsonProperty("deducted_point")
    private Float deductedPoint;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CourseDTO course;

    public TypicalMistakeDTO(Long id,
                             String title,
                             String description,
                             Float deductedPoint) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deductedPoint = deductedPoint;
    }
}
