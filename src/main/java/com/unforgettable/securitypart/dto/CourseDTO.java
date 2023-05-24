package com.unforgettable.securitypart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unforgettable.securitypart.entity.Course;
import com.unforgettable.securitypart.entity.Task;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    @JsonProperty("start_date")
    private Timestamp startDate;

    @JsonProperty("end_date")
    private Timestamp endDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String educator;

    //    @JsonProperty(value = "students_count")
//    private Integer studentsCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Task> tasks;

    public CourseDTO(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public CourseDTO(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.description = course.getDescription();
        this.educator = course.getEducator().getFirstname()
                + " " + course.getEducator().getLastname();
        this.tasks = course.getTasks();
        this.startDate = course.getStartDate();
        this.endDate = course.getEndDate();
//        this.studentsCount = studentsCount;
    }

    public CourseDTO(Long id, String title, String description, Timestamp startDate, Timestamp endDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
