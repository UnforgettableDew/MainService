package com.unforgettable.securitypart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unforgettable.securitypart.dto.*;
import com.unforgettable.securitypart.entity.*;
import com.unforgettable.securitypart.model.request.AssessRequest;
import com.unforgettable.securitypart.model.request.GithubAccessToken;
import com.unforgettable.securitypart.model.response.CommonResponse;
import com.unforgettable.securitypart.service.EducatorService;
import com.unforgettable.securitypart.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
@RequestMapping("/educator")
@CrossOrigin(origins = "*",
        allowedHeaders = "*",
        exposedHeaders = "*",
        methods = {GET, POST, PUT, DELETE},
        maxAge = 3600)
public class EducatorController {
    private final EducatorService educatorService;
    private final FileService fileService;

    @Autowired
    public EducatorController(EducatorService educatorService, FileService fileService) {
        this.educatorService = educatorService;
        this.fileService = fileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<Educator> getEducatorProfile(HttpServletRequest request) {
        return new ResponseEntity<>(educatorService.getEducatorProfile(request), OK);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getEducatorCourses(HttpServletRequest request) {
        return new ResponseEntity<>(educatorService.getEducatorCourses(request), OK);
    }

    @GetMapping("/course/{courseId}/typical-mistakes")
    public ResponseEntity<List<TypicalMistakeDTO>> getCourseTypicalMistakes(HttpServletRequest request,
                                                                            @PathVariable Long courseId) {
        return new ResponseEntity<>(educatorService.getCourseTypicalMistakes(request, courseId), OK);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<CourseDTO> getEducatorCourse(HttpServletRequest request,
                                                       @PathVariable Long courseId) {
        return new ResponseEntity<>(educatorService.getEducatorCourse(request, courseId), OK);
    }

    @GetMapping("/course/{courseId}/students")
    public ResponseEntity<List<StudentDTO>> getListOfStudentsByCourse(HttpServletRequest request,
                                                                      @PathVariable Long courseId) {
        return new ResponseEntity<>(educatorService.getListOfStudentsByCourse(request, courseId), OK);
    }

    @GetMapping("/course/{courseId}/students/{studentId}")
    public ResponseEntity<StudentDTO> getStudentProfileByCourse(HttpServletRequest request,
                                                                @PathVariable Long courseId,
                                                                @PathVariable Long studentId) {
        return new ResponseEntity<>(educatorService.getStudentProfile(request, courseId, studentId), OK);
    }

    @GetMapping("/course/{courseId}/task/{taskId}/students")
    public ResponseEntity<List<StudentDTO>> getStudentsWhoPassedTask(HttpServletRequest request,
                                                                     @PathVariable Long courseId,
                                                                     @PathVariable Long taskId) {
        return new ResponseEntity<>(educatorService.getStudentsWhoPassedTask(request, courseId, taskId), OK);
    }

    @GetMapping("/course/{courseId}/student/{studentId}/task/{taskId}")
    public ResponseEntity<PassedTaskDTO> getStudentPassedTask(HttpServletRequest request,
                                                              @PathVariable Long courseId,
                                                              @PathVariable Long studentId,
                                                              @PathVariable Long taskId) {
        return new ResponseEntity<>(educatorService.
                getPassedTaskByCourseAndStudent(request, courseId, studentId, taskId), OK);
    }

    @GetMapping("/course/{courseId}/task/{taskId}")
    public ResponseEntity<TaskDTO> getTaskByCourse(HttpServletRequest request,
                                                   @PathVariable Long courseId,
                                                   @PathVariable Long taskId) {
        return new ResponseEntity<>(educatorService.getTaskByCourse(request, courseId, taskId), OK);
    }

    @GetMapping("/course/{courseId}/tasks")
    public ResponseEntity<List<TaskDTO>> getCourseTasks(HttpServletRequest request,
                                                        @PathVariable Long courseId) {
        return new ResponseEntity<>(educatorService.getCourseTasks(request, courseId), OK);
    }

    @GetMapping("/course/{courseId}/student/{studentId}/task/{taskId}/download")
    public ResponseEntity<Resource> downloadStudentPassedTask(HttpServletRequest request,
                                                              @PathVariable Long courseId,
                                                              @PathVariable Long studentId,
                                                              @PathVariable Long taskId) throws MalformedURLException {
        Resource resource = educatorService.downloadPassedTask(request, courseId, studentId, taskId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/course/{courseId}/stats")
    public ResponseEntity<Map<String, Object>> getCourseStats(HttpServletRequest request,
                                                              @PathVariable Long courseId) {
        return new ResponseEntity<>(educatorService.courseStats(request, courseId), OK);
    }

    @GetMapping("/course/{courseId}/unchecked-tasks")
    public ResponseEntity<List<StudentDTO>> getStudentsWithUncheckedPassedTasks(HttpServletRequest request,
                                                                                @PathVariable Long courseId) {
        return new ResponseEntity<>(educatorService.getStudentsWithUncheckedPassedTasks(request, courseId), OK);
    }

    @GetMapping("/course/{courseId}/student/{studentId}/task/{passedTaskId}/commits")
    public ResponseEntity<List<Object>> getStudentPassedTaskCommits(HttpServletRequest request,
                                                                    @PathVariable Long courseId,
                                                                    @PathVariable Long studentId,
                                                                    @PathVariable Long passedTaskId) {
        return new ResponseEntity<>(educatorService.getCommitList(request, courseId, studentId, passedTaskId), OK);
    }

    @GetMapping("/course/{courseId}/student/{studentId}/task/{passedTaskId}/check-basic-files")
    public ResponseEntity<List<Object>> checkRepoBasicFiles(HttpServletRequest request,
                                                            @PathVariable Long courseId,
                                                            @PathVariable Long studentId,
                                                            @PathVariable Long passedTaskId) {
        return new ResponseEntity<>(educatorService.checkFileExistence(request, courseId, studentId, passedTaskId), OK);
    }

    @GetMapping("/course/{courseId}/task/{taskId}/deadline-tasks")
    public ResponseEntity<List<StudentDTO>> getStudentsWhoDidntSubmitTaskOnTime(HttpServletRequest request,
                                                                                @PathVariable Long courseId,
                                                                                @PathVariable Long taskId){
        return new ResponseEntity<>(educatorService.getStudentsWhoDidntSubmitTaskOnTime(request, courseId, taskId), OK);
    }

    @PostMapping("/course/create")
    public ResponseEntity<Course> createCourse(HttpServletRequest request,
                                               @RequestBody Course course) {

        return new ResponseEntity<>(educatorService.createCourse(request, course), CREATED);
    }

    @PostMapping("/course/{courseId}/task/create")
    public ResponseEntity<Task> createTask(HttpServletRequest request,
                                           @PathVariable Long courseId,
                                           @RequestBody Task task) {
        return new ResponseEntity<>(educatorService.addTask(request, courseId, task), CREATED);
    }

    @PostMapping("/profile/create")
    public ResponseEntity<Educator> createProfile(HttpServletRequest request,
                                                  @RequestBody Educator educator) {
        return new ResponseEntity<>(educatorService.createProfile(request, educator), CREATED);
    }

//    @GetMapping("/createdir")
//    public void createDir() {
//        fileService.createDirectories();
//    }

    @PostMapping("/course/{courseId}/task/{taskId}/upload")
    public ResponseEntity<CommonResponse> uploadTask(HttpServletRequest request,
                                                     @PathVariable Long courseId,
                                                     @PathVariable Long taskId,
                                                     @RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(educatorService.uploadTask(request, courseId, taskId, file), OK);
    }

    @PostMapping("/course/{courseId}/typical-mistakes/add")
    public ResponseEntity<CommonResponse> addTypicalMistake(HttpServletRequest request,
                                                            @PathVariable Long courseId,
                                                            @RequestBody TypicalMistake typicalMistake) {
        return new ResponseEntity<>(educatorService.addTypicalMistake(request, courseId, typicalMistake), OK);
    }

    @PostMapping("/course/{courseId}/student/{studentId}/task/{passedTaskId}")
    public ResponseEntity<PassedTaskDTO> assessStudentPassedTask(HttpServletRequest request,
                                                                 @PathVariable Long courseId,
                                                                 @PathVariable Long studentId,
                                                                 @PathVariable Long passedTaskId,
                                                                 @RequestBody AssessRequest assessRequest) {
        return new ResponseEntity<>
                (educatorService.assessStudentPassedTask(request, courseId, studentId, passedTaskId, assessRequest), OK);
    }

    @DeleteMapping("/course/{courseId}/typical-mistake/{typicalMistakeId}/delete")
    public ResponseEntity<CommonResponse> deleteTypicalMistake(HttpServletRequest request,
                                                               @PathVariable Long courseId,
                                                               @PathVariable Long typicalMistakeId) {
        return new ResponseEntity<>(educatorService.deleteTypicalMistake(request, courseId, typicalMistakeId), OK);
    }

    @DeleteMapping("/course/{courseId}/task/{taskId}/delete")
    public ResponseEntity<CommonResponse> deleteTask(HttpServletRequest request,
                                                     @PathVariable Long courseId,
                                                     @PathVariable Long taskId) {
        return new ResponseEntity<>(educatorService.deleteTask(request, courseId, taskId), OK);
    }

    @DeleteMapping("/course/{courseId}/student/{studentId}/kick")
    public ResponseEntity<CommonResponse> kickStudent(HttpServletRequest request,
                                                      @PathVariable Long courseId,
                                                      @PathVariable Long studentId) {
        return new ResponseEntity<>(educatorService.kickStudent(request, courseId, studentId), OK);
    }

    @DeleteMapping("/course/{courseId}/delete")
    public ResponseEntity<CommonResponse> deleteCourse(HttpServletRequest request,
                                                       @PathVariable Long courseId) {
        return new ResponseEntity<>(educatorService.deleteCourse(request, courseId), OK);
    }

    @PutMapping("/course/{courseId}/edit")
    public ResponseEntity<CommonResponse> editCourse(HttpServletRequest request,
                                                     @PathVariable Long courseId,
                                                     @RequestBody Course course) {
        return new ResponseEntity<>(educatorService.editCourse(request, courseId, course), OK);
    }

    @PutMapping("/profile/edit")
    public ResponseEntity<CommonResponse> editProfile(HttpServletRequest request,
                                                      @RequestBody Educator educator) {
        return new ResponseEntity<>(educatorService.editProfile(request, educator), OK);
    }

    @PutMapping("/course/{courseId}/task/{taskId}/edit")
    public ResponseEntity<CommonResponse> editTask(HttpServletRequest request,
                                                   @PathVariable Long courseId,
                                                   @PathVariable Long taskId,
                                                   @RequestBody Task task) {
        return new ResponseEntity<>(educatorService.editTask(request, courseId, taskId, task), OK);
    }

    @PutMapping("/course/{courseId}/student/{studentId}/task/{taskId}/edit")
    public ResponseEntity<CommonResponse> editStudentPassedTask(HttpServletRequest request,
                                                                @PathVariable Long courseId,
                                                                @PathVariable Long studentId,
                                                                @PathVariable Long taskId,
                                                                @RequestBody PassedTask passedTask) {
        return new ResponseEntity<>(educatorService.editPassedTask(request, courseId, taskId, studentId, passedTask), OK);
    }

    @PutMapping("/course/{courseId}/typical-mistake/{typicalMistakeId}/edit")
    public ResponseEntity<CommonResponse> editTypicalMistake(HttpServletRequest request,
                                                   @PathVariable Long courseId,
                                                   @PathVariable Long typicalMistakeId,
                                                   @RequestBody TypicalMistake typicalMistake) {
        return new ResponseEntity<>(educatorService
                .editTypicalMistake(request, courseId, typicalMistakeId, typicalMistake), OK);
    }

    @PostMapping("/course/{courseId}/task/{taskId}/repo/create")
    public ResponseEntity<Object> createRepo(HttpServletRequest request,
                                             @PathVariable Long courseId,
                                             @PathVariable Long taskId,
                                             @RequestBody Object repo) throws JsonProcessingException {
        return new ResponseEntity<>(educatorService.createRepo(request,courseId, taskId, repo), OK);
    }

    @PostMapping("/course/{courseId}/task/{taskId}/file-to-check/add")
    public ResponseEntity<FileToCheck> addFileToCheck(HttpServletRequest request,
                                                      @PathVariable Long courseId,
                                                      @PathVariable Long taskId,
                                                      @RequestBody FileToCheck fileToCheck){
        return new ResponseEntity<>(educatorService.addFileToCheck(request, courseId, taskId, fileToCheck), CREATED);
    }

    @GetMapping("/github/provide-access")
    public ResponseEntity<Map<String, String>> provideAccessToken(HttpServletRequest request) {
        return new ResponseEntity<>(educatorService.provideAccessToGithub(request), OK);
    }

    @PutMapping("/github/save-access-token")
    public ResponseEntity<CommonResponse> saveGithubAccessToken(HttpServletRequest request,
                                                                @RequestBody GithubAccessToken githubAccessToken){
        return new ResponseEntity<>(educatorService.saveAccessToken(request,githubAccessToken), OK);
    }

    @GetMapping("/course/{courseId}/student/{studentId}/task/{taskId}/check-files")
    public ResponseEntity<List<Object>> checkFilesExistence(HttpServletRequest request,
                                                            @PathVariable Long courseId,
                                                            @PathVariable Long studentId,
                                                            @PathVariable Long taskId){
        return new ResponseEntity<>(educatorService.checkFilesExistence(request, courseId, studentId, taskId), OK);
    }
}
