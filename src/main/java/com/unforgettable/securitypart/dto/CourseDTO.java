package com.unforgettable.securitypart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unforgettable.securitypart.entity.Course;
import com.unforgettable.securitypart.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
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
//        this.studentsCount = studentsCount;
    }
}