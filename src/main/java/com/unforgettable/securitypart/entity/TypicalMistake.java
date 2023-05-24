package com.unforgettable.securitypart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "typical_mistake")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TypicalMistake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "deducted_point")
    @JsonProperty("deducted_point")
    private Float deductedPoint;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;

    public void updateTypicalMistake(TypicalMistake typicalMistake) {
        if (typicalMistake.getTitle() != null)
            this.title = typicalMistake.getTitle();
        if (typicalMistake.getDescription() != null)
            this.description = typicalMistake.getDescription();
        if (typicalMistake.getDeductedPoint() != null)
            this.deductedPoint = typicalMistake.getDeductedPoint();
    }
}
