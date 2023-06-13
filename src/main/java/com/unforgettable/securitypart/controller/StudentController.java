package com.unforgettable.securitypart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unforgettable.securitypart.dto.*;
import com.unforgettable.securitypart.entity.*;
import com.unforgettable.securitypart.model.request.GithubAccessToken;
import com.unforgettable.securitypart.model.response.CommonResponse;
import com.unforgettable.securitypart.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "*",
        allowedHeaders = "*",
        exposedHeaders = "*",
        methods = {GET, POST, PUT, DELETE},
        maxAge = 3600)
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

    @GetMapping("/course/{courseId}")
    public ResponseEntity<CourseDTO> getStudentCourse(HttpServletRequest request,
                                                      @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.getStudentCourse(request, courseId), OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<StudentDTO> getProfile(HttpServletRequest request) {
        return new ResponseEntity<>(studentService.getProfile(request), OK);
    }

    @GetMapping("/course/{courseId}/educator-profile")
    public ResponseEntity<EducatorDTO> getEducator(HttpServletRequest request,
                                                @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.getEducatorProfile(request, courseId),OK);
    }

    @GetMapping("/course/{courseId}/typical-mistakes")
    public ResponseEntity<List<TypicalMistakeDTO>> getCourseTypicalMistakes(HttpServletRequest request,
                                                                            @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.getCourseTypicalMistakes(request, courseId), OK);
    }

    @GetMapping("/courses/{courseId}/passed-tasks")
    public ResponseEntity<List<PassedTaskDTO>> getStudentCoursePassedTasks(HttpServletRequest request,
                                                                           @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.getStudentPassedTasks(request, courseId), OK);
    }

    @GetMapping("/course/{courseId}/task/{taskId}/passed")
    public ResponseEntity<PassedTaskDTO> getPassedTaskByCourse(HttpServletRequest request,
                                                               @PathVariable Long courseId,
                                                               @PathVariable Long taskId) {
        return new ResponseEntity<>(studentService.getPassedTaskByCourse(request, courseId, taskId), OK);
    }

    @GetMapping("/course/{courseId}/task/{taskId}")
    public ResponseEntity<TaskDTO> getTaskByCourse(HttpServletRequest request,
                                                   @PathVariable Long courseId,
                                                   @PathVariable Long taskId) {
        return new ResponseEntity<>(studentService.getTaskByCourse(request, courseId, taskId), OK);
    }

    @GetMapping("/courses/{courseId}/stats")
    public ResponseEntity<Map<String, Object>> passedTasksStats(HttpServletRequest request,
                                                                @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.passedTasksStats(request, courseId), OK);
    }

    @PostMapping("/profile/create")
    public ResponseEntity<Student> createProfile(HttpServletRequest request,
                                                 @RequestBody Student student) {
        return new ResponseEntity<>(studentService.createProfile(request, student), CREATED);
    }

    @PostMapping("/course/{courseId}/join")
    public ResponseEntity<CommonResponse> joinCourse(HttpServletRequest request,
                                                     @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.joinCourse(request, courseId), OK);
    }

    @PostMapping("/course/{courseId}/task/{taskId}")
    public ResponseEntity<CommonResponse> addPassedTask(HttpServletRequest request,
                                                        @RequestBody PassedTask passedTask,
                                                        @PathVariable Long courseId,
                                                        @PathVariable Long taskId) {
        return new ResponseEntity<>(studentService.addPassedTask(request, passedTask, courseId, taskId), OK);
    }

    @PostMapping("/course/{courseId}/task/{taskId}/upload")
    public ResponseEntity<CommonResponse> uploadPassedTask(HttpServletRequest request,
                                                           @RequestParam("file") MultipartFile file,
                                                           @PathVariable Long courseId,
                                                           @PathVariable Long taskId) throws IOException {
        return new ResponseEntity<>(studentService.uploadPassedTaskFile(request, file, courseId, taskId), OK);
    }

    @PostMapping("/course/{courseId}/task/{taskId}/repo/create")
    public ResponseEntity<Object> createRepo(HttpServletRequest request,
                                             @PathVariable Long courseId,
                                             @PathVariable Long taskId,
                                             @RequestBody Object repo) throws JsonProcessingException {
        return new ResponseEntity<>(studentService.createRepo(request, courseId, taskId, repo), OK);
    }

    @GetMapping("/courses/{courseId}/task/{taskId}/download")
    public ResponseEntity<Resource> downloadTask(HttpServletRequest request,
                                                 @PathVariable Long courseId,
                                                 @PathVariable Long taskId) throws MalformedURLException {
        Resource resource = studentService.downloadTask(request, courseId, taskId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/course/{courseId}/task/{taskId}/download")
    public ResponseEntity<Resource> downloadStudentPassedTask(HttpServletRequest request,
                                                              @PathVariable Long courseId,
                                                              @PathVariable Long taskId) throws MalformedURLException {
        Resource resource = studentService.downloadPassedTask(request, courseId, taskId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/course/{courseId}/tasks")
    public ResponseEntity<List<TaskDTO>> getCourseTasks(HttpServletRequest request,
                                                        @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.getCourseTasks(request, courseId), OK);
    }

    @DeleteMapping("/courses/{courseId}/task/{taskId}/delete")
    public ResponseEntity<CommonResponse> deletePassedTask(HttpServletRequest request,
                                                           @PathVariable Long courseId,
                                                           @PathVariable Long taskId) {
        return new ResponseEntity<>(studentService.deletePassedTask(request, courseId, taskId), OK);
    }

    @DeleteMapping("/courses/{courseId}/task/{taskId}/file/delete")
    public ResponseEntity<CommonResponse> deletePassedTaskFile(HttpServletRequest request,
                                                           @PathVariable Long courseId,
                                                           @PathVariable Long taskId) {
        return new ResponseEntity<>(studentService.deletePassedTaskFile(request, courseId, taskId), OK);
    }

    @PutMapping("/profile/edit")
    public ResponseEntity<CommonResponse> editProfile(HttpServletRequest request,
                                                      @RequestBody Student student) {
        return new ResponseEntity<>(studentService.editStudentProfile(request, student), OK);
    }

    @PutMapping("/course/{courseId}/task/{taskId}/edit")
    public ResponseEntity<CommonResponse> editPassedTask(HttpServletRequest request,
                                                         @PathVariable Long courseId,
                                                         @PathVariable Long taskId,
                                                         @RequestBody PassedTask passedTask) {
        return new ResponseEntity<>(studentService.editPassedTask(request, courseId, taskId, passedTask), OK);
    }

    @PutMapping("/course/{courseId}/task/{taskId}/repo/import")
    public ResponseEntity<Object> importRepo(HttpServletRequest request,
                                             @PathVariable Long courseId,
                                             @PathVariable Long taskId) {
        return new ResponseEntity<>(studentService.importRepo(request, courseId, taskId), OK);
    }

    @GetMapping("/github/provide-access")
    public ResponseEntity<Map<String, String>> provideAccessToken(HttpServletRequest request) {
        return new ResponseEntity<>(studentService.provideAccessToGithub(request), OK);
    }

    @GetMapping("/course/{courseId}/task/{taskId}/files-to-check")
    public ResponseEntity<List<FileToCheck>> getFilesToCheck(HttpServletRequest request,
                                                             @PathVariable Long courseId,
                                                             @PathVariable Long taskId){
        return new ResponseEntity<>(studentService.getFileToCheck(request, courseId, taskId), OK);
    }

    @PostMapping("/github/save-access-token")
    public ResponseEntity<CommonResponse> saveGithubAccessToken(HttpServletRequest request,
                                                                @RequestBody GithubAccessToken githubAccessToken) {
        return new ResponseEntity<>(studentService.saveAccessToken(request, githubAccessToken), OK);
    }
}