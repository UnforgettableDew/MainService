package com.unforgettable.securitypart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "student_group")
    private String group;

    @Column(name = "email")
    private String email;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "telegram_contact")
    @JsonProperty(value = "telegram_contact")
    private String telegramContact;

    @Column(name = "github_access_token")
    @JsonProperty(value = "github_access_token")
    @JsonIgnore
    private String githubAccessToken;

    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    @JsonIgnore
    private List<PassedTask> passedTasks;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "course_student",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonManagedReference
    @JsonIgnore
    private List<Course> courses;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    private UserEntity user;

    @Column(name = "registration_date")
    @JsonProperty(value = "registration_date")
    private Timestamp registrationDate;

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void addLaboratoryWork(PassedTask passedTask) {
        passedTasks.add(passedTask);
    }

    public void updateStudent(Student student) {
        if (student.getFirstname() != null)
            this.firstname = student.getFirstname();
        if (student.getLastname() != null)
            this.lastname = student.getLastname();
        if (student.getEmail() != null)
            this.email = student.getEmail();
        if (student.getGroup() != null)
            this.group = student.getGroup();
        if (student.getTelegramContact() != null)
            this.telegramContact = student.getTelegramContact();
        if (student.getBirthday() != null)
            this.birthday = student.getBirthday();
    }
}
