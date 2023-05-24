package com.unforgettable.securitypart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unforgettable.securitypart.entity.PassedTask;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PassedTaskDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String reference;
    @JsonProperty(value = "github_reference")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String githubReference;
    private Float point;
    @JsonProperty("is_assessed")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isAssessed;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("educator_comment")
    private String educatorComment;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("student_comment")
    private String studentComment;

    @JsonProperty("submission_date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp submissionDate;
    private TaskDTO task;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String course;

    @JsonProperty("students_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer studentsCount;

    public PassedTaskDTO(PassedTask passedTask) {
        this.id = passedTask.getId();
        this.reference = passedTask.getReference();
        this.githubReference = passedTask.getGithubReference();
        this.task = new TaskDTO(passedTask.getTask());
        this.point = passedTask.getPoint();
        this.submissionDate = passedTask.getSubmissionDate();
        this.isAssessed = passedTask.getIsAssessed();
        this.course = passedTask.getTask().getCourse().getTitle();
        this.educatorComment = passedTask.getEducatorComment();
        this.studentComment = passedTask.getStudentComment();
    }

    public PassedTaskDTO(Long id, Float point, Boolean isAssessed) {
        this.id = id;
        this.point = point;
        this.isAssessed = isAssessed;
    }

    public PassedTaskDTO(Long id, Float point, Boolean isAssessed, String githubReference) {
        this.id = id;
        this.point = point;
        this.isAssessed = isAssessed;
        this.githubReference = githubReference;
    }

    public PassedTaskDTO(Long id,
                         String reference,
                         String githubReference,
                         Float point,
                         Boolean isAssessed,
                         String educatorComment,
                         String studentComment,
                         Timestamp submissionDate) {
        this.id = id;
        this.reference = reference;
        this.githubReference = githubReference;
        this.point = point;
        this.isAssessed = isAssessed;
        this.educatorComment = educatorComment;
        this.studentComment = studentComment;
        this.submissionDate = submissionDate;
    }

    public PassedTaskDTO(Float point) {
        this.point = point;
    }
}
