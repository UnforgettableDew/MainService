package com.unforgettable.securitypart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.unforgettable.securitypart.entity.Course;
import com.unforgettable.securitypart.entity.PassedTask;
import com.unforgettable.securitypart.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonPropertyOrder({
        "id",
        "firstname",
        "lastname",
        "group",
        "age",
        "email",
        "telegram_contact",
        "registration_date",
        "courses"})
public class StudentDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String group;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date birthday;
    @JsonProperty(value = "registration_date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp registrationDate;
    @JsonProperty(value = "telegram_contact")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String telegramContact;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> courses;
    @JsonProperty(value = "passed_tasks")
    private List<PassedTaskDTO> passedTasks;

    public StudentDTO(Student student) {
        this.id = student.getId();
        this.firstname = student.getFirstname();
        this.lastname = student.getLastname();
        this.group = student.getGroup();
        this.email = student.getEmail();
        this.birthday = student.getBirthday();
        this.registrationDate = student.getRegistrationDate();
        this.telegramContact = student.getTelegramContact();
        this.courses = student.getCourses()
                .stream()
                .map(Course::getTitle).toList();
        this.passedTasks =student.getPassedTasks()
                .stream().map(PassedTaskDTO::new).toList();
    }

    public StudentDTO(Long id,
                      String firstname,
                      String lastname,
                      String group,
                      String email,
                      Date birthday,
                      Timestamp registrationDate,
                      String telegramContact) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.group = group;
        this.email = email;
        this.birthday = birthday;
        this.registrationDate = registrationDate;
        this.telegramContact = telegramContact;
        this.passedTasks = new ArrayList<>();
    }

    public StudentDTO(Long studentId,
                      String firstname,
                      String lastname,
                      String group) {
        this.id = studentId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.group = group;
        this.passedTasks = new ArrayList<>();
    }


    public void addPassedTask(PassedTask passedTask) {
        passedTasks.add(new PassedTaskDTO(passedTask));
    }

    public void addPassedTask(PassedTaskDTO laboratoryWork) {
        passedTasks.add(laboratoryWork);
    }
}
