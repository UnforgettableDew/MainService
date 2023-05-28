package com.unforgettable.securitypart.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "task")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reference")
    @JsonIgnore
    private String reference;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "max_point")
    @JsonProperty("max_point")
    private Float maxPoint;

    @Column(name = "start_date")
    @JsonProperty("start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    @JsonProperty("end_date")
    private Timestamp endDate;

    @Column(name = "github_reference")
    private String githubReference;
    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonBackReference
    @JsonIgnore
    private Course course;

    @OneToMany(mappedBy = "task")
    @JsonManagedReference
    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FileToCheck> filesToCheck;

    @OneToMany(mappedBy = "task")
    @JsonBackReference
    @JsonIgnore
    private List<PassedTask> passedTasks;

    public Task(Long id) {
        this.id = id;
    }

    public void updateTask(Task task) {
        if (task.getDescription() != null)
            this.description = task.getDescription();
        if (task.getTitle() != null)
            this.title = task.getTitle();
        if (task.getStartDate() != null)
            this.startDate = task.getStartDate();
        if (task.getEndDate() != null)
            this.endDate = task.getEndDate();
    }
}
