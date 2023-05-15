package com.unforgettable.securitypart.controller;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.LaboratoryWorkDTO;
import com.unforgettable.securitypart.entity.LaboratoryWork;
import com.unforgettable.securitypart.entity.Student;
import com.unforgettable.securitypart.model.CommonResponse;
import com.unforgettable.securitypart.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getStudentCourses(HttpServletRequest request) {
        return new ResponseEntity<>(studentService.getStudentCourses(request), OK);
    }

    @GetMapping("/courses/{courseId}/labs")
    public ResponseEntity<List<LaboratoryWorkDTO>> getStudentCourseLaboratoryWork(HttpServletRequest request,
                                                                                  @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.getStudentLaboratoryWorks(request, courseId), OK);
    }

    @GetMapping("/courses/{courseId}/labs/{lwId}")
    public ResponseEntity<LaboratoryWorkDTO> getLaboratoryWorkByCourse(HttpServletRequest request,
                                                                       @PathVariable Long courseId,
                                                                       @PathVariable Long lwId) {
        return new ResponseEntity<>(studentService.getLaboratoryWorkByCourse(request, courseId, lwId), OK);
    }

    @GetMapping("/courses/{courseId}/passed-lw-info")
    public ResponseEntity<Map<String, Object>> passedLWStats(HttpServletRequest request,
                                                             @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.passedLabsStats(request, courseId), OK);
    }

    @PostMapping("/profile/create")
    public ResponseEntity<CommonResponse> createProfile(HttpServletRequest request,
                                                        @RequestBody Student student) {
        return new ResponseEntity<>(studentService.createProfile(request, student), OK);
    }

    @PostMapping("/course/{courseId}/join")
    public ResponseEntity<CommonResponse> joinCourse(HttpServletRequest request,
                                                     @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.joinCourse(request, courseId), OK);
    }

    @PostMapping("/course/{courseId}/task/{taskId}")
    public ResponseEntity<CommonResponse> addLaboratoryWork(HttpServletRequest request,
                                                            @RequestBody LaboratoryWork laboratoryWork,
                                                            @PathVariable Long courseId,
                                                            @PathVariable Long taskId) {
        return new ResponseEntity<>(studentService.addLaboratoryWork(request, laboratoryWork, courseId, taskId), OK);
    }

    @PutMapping("/profile/edit")
    public ResponseEntity<CommonResponse> editProfile(HttpServletRequest request,
                                                      @RequestBody Student student){
        return new ResponseEntity<>(studentService.editStudentProfile(request, student), OK);
    }
}
