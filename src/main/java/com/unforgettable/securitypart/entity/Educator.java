package com.unforgettable.securitypart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "educator")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Educator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "email")
    private String email;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "telegram_contact")
    @JsonProperty(value = "telegram_contact")
    private String telegramContact;

    @Column(name = "github_access_token")
    @JsonProperty(value = "github_access_token")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String githubAccessToken;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "educator")
    @JsonManagedReference
    @JsonIgnore
    private List<Course> courses;

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void updateEducator(Educator educator) {
        if (educator.getFirstname() != null)
            this.firstname = educator.getFirstname();
        if (educator.getLastname() != null)
            this.lastname = educator.getLastname();
        if (educator.getBirthday() != null)
            this.birthday = educator.getBirthday();
        if (educator.getEmail() != null)
            this.email = educator.getEmail();
        if (educator.getTelegramContact() != null)
            this.telegramContact = educator.getTelegramContact();
    }

    public Educator(String firstname, String lastname, String email, Date birthday, String telegramContact) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.birthday = birthday;
        this.telegramContact = telegramContact;
    }
}
