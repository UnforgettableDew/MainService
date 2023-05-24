package com.unforgettable.securitypart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class TaskDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonProperty("max_point")
    private Float maxPoint;
    @JsonProperty("start_date")
    @JsonInclude(JsonInclude.Include.NON_NULL)

    private Timestamp startDate;
    @JsonProperty("end_date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp endDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PassedTaskDTO> laboratoryWorks;

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.maxPoint = task.getMaxPoint();
        this.startDate = task.getStartDate();
        this.endDate = task.getEndDate();
    }

//    public TaskDTO(Long id, Float maxScore) {
//        this.id = id;
//        this.maxScore = maxScore;
//    }

    public TaskDTO(Long id, String title, Float maxPoint) {
        this.id = id;
        this.title = title;
        this.maxPoint = maxPoint;
    }

    public TaskDTO(Long id,
                   String title,
                   String description,
                   Float maxPoint,
                   Timestamp startDate,
                   Timestamp endDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.maxPoint = maxPoint;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
