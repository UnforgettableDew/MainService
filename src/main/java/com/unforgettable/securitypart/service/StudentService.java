package com.unforgettable.securitypart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unforgettable.securitypart.dto.*;
import com.unforgettable.securitypart.entity.*;
import com.unforgettable.securitypart.exception.NoPassedTaskException;
import com.unforgettable.securitypart.exception.NoSuchStudentOnCourseException;
import com.unforgettable.securitypart.feign.GithubFeign;
import com.unforgettable.securitypart.model.request.GithubAccessToken;
import com.unforgettable.securitypart.model.request.RepositoryImportRequest;
import com.unforgettable.securitypart.model.response.CommonResponse;
import com.unforgettable.securitypart.repository.*;
import com.unforgettable.securitypart.utils.EducationUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final PassedTaskRepository passedTaskRepository;
    private final TaskRepository taskRepository;
    private final EducatorRepository educatorRepository;
    private final TypicalMistakeRepository typicalMistakeRepository;
    private final JwtService jwtService;
    private final EducationUtils educationUtils;
    private final FileService fileService;
    private final GithubFeign githubFeign;

    private final RestTemplate restTemplate;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          CourseRepository courseRepository,
                          PassedTaskRepository passedTaskRepository,
                          TaskRepository taskRepository,
                          EducatorRepository educatorRepository,
                          TypicalMistakeRepository typicalMistakeRepository,
                          JwtService jwtService,
                          EducationUtils educationUtils, FileService fileService, GithubFeign githubFeign, RestTemplate restTemplate) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.passedTaskRepository = passedTaskRepository;
        this.taskRepository = taskRepository;
        this.educatorRepository = educatorRepository;
        this.typicalMistakeRepository = typicalMistakeRepository;
        this.jwtService = jwtService;
        this.educationUtils = educationUtils;
        this.fileService = fileService;
        this.githubFeign = githubFeign;
        this.restTemplate = restTemplate;
    }

    public StudentDTO getProfile(HttpServletRequest request) {
        Long studentId = jwtService.getStudentId(request);
        return studentRepository.findStudentProfileById(studentId);
    }

    public List<CourseDTO> getStudentCourses(HttpServletRequest request) {
        Long studentId = jwtService.getStudentId(request);

        return courseRepository.findCoursesByStudentId(studentId);
    }

    public CourseDTO getStudentCourse(HttpServletRequest request, Long courseId) {
        Long studentId = educationUtils.getStudentId(request, courseId);

        return new CourseDTO(courseRepository.findCourseByStudentId(studentId, courseId));
    }

    public TaskDTO getTaskByCourse(HttpServletRequest request,
                                   Long courseId, Long taskId) {
        Long studentId = educationUtils.getStudentId(request, courseId);
        return taskRepository.findFullTaskInfoById(taskId);
    }

    public List<TypicalMistakeDTO> getCourseTypicalMistakes(HttpServletRequest request,
                                                            Long courseId) {
        Long studentId = educationUtils.getStudentId(request, courseId);
        return typicalMistakeRepository.findTypicalMistakesByCourseId(courseId);
    }


    public List<PassedTaskDTO> getStudentPassedTasks(HttpServletRequest request,
                                                     Long courseId) {
        Long studentId = educationUtils.getStudentId(request, courseId);

        List<PassedTaskDTO> passedTasks = passedTaskRepository.
                findPassedTasksByStudentIdAndCourseId(studentId, courseId);

        passedTasks.forEach(passedTaskDTO ->
                passedTaskDTO.setTask(taskRepository.
                        findTaskByPassedTaskId(passedTaskDTO.getId())));
        return passedTasks;
    }

    public PassedTaskDTO getPassedTaskByCourse(HttpServletRequest request,
                                               Long courseId,
                                               Long taskId) {
        Long studentId = educationUtils.getStudentId(request, courseId);

        PassedTaskDTO passedTask = passedTaskRepository
                .findPassedTasksByStudentIdAndCourseIdAndTaskId(
                        studentId,
                        courseId,
                        taskId);

        if (passedTask == null)
            throw new NoPassedTaskException("No such passed task with task id = " + taskId
                    + " on course with id = " + courseId);

        passedTask.setTask(taskRepository.findFullTaskInfoById(taskId));

        return passedTask;
    }

    public Map<String, Object> passedTasksStats(HttpServletRequest request,
                                                Long courseId) {

        Map<String, Object> passedTaskStats = new HashMap<>();

        Long studentId = educationUtils.getStudentId(request, courseId);
        Integer tasksCount = taskRepository.countTaskByCourseId(courseId);

        passedTaskStats.put("tasks_count", tasksCount);
        passedTaskStats.put("passed_tasks", getStudentPassedTasks(request, courseId));

        return passedTaskStats;
    }

    public CommonResponse addPassedTask(HttpServletRequest request,
                                        PassedTask passedTask,
                                        Long courseId,
                                        Long taskId) {
        Long studentId = educationUtils.getStudentId(request, courseId);
        Student student = studentRepository.findById(studentId).get();
        Task task = taskRepository.findById(taskId).get();

        passedTask.setTask(task);
        passedTask.setPoint(null);
        passedTask.setIsAssessed(false);
        passedTask.setStudent(student);
        passedTask.setSubmissionDate(Timestamp.valueOf(LocalDateTime.now()));

        passedTaskRepository.save(passedTask);
        return new CommonResponse(true);
    }

    public CommonResponse uploadPassedTaskFile(HttpServletRequest request,
                                               MultipartFile file,
                                               Long courseId,
                                               Long taskId) throws IOException {

        Long studentId = educationUtils.getStudentId(request, courseId);
        Student student = studentRepository.findById(studentId).get();

        Task task = taskRepository.findById(taskId).get();

        PassedTask passedTask = passedTaskRepository.findPassedTaskByTaskIdAndStudentId(taskId, studentId);

        String educatorName = educatorRepository.findEducatorNameByCourseId(courseId).replace(",", "");

        String course = courseRepository.findCourseTitleById(courseId);

        String path = "D:\\Files\\" + educatorName +
                "\\" + course +
                "\\Students\\" + student.getFirstname() + student.getLastname() +
                "\\Labs\\";

        passedTask.setReference(path + file.getOriginalFilename());
        passedTask.setTask(task);
        passedTask.setPoint(null);
        passedTask.setIsAssessed(false);
        passedTask.setStudent(student);
        passedTaskRepository.save(passedTask);


        Path filePath = Paths.get(path, file.getOriginalFilename());
        file.transferTo(filePath.toFile());
        return new CommonResponse(true);
    }

    public Resource downloadTask(HttpServletRequest request,
                                 Long courseId,
                                 Long taskId) throws MalformedURLException {
        Long studentId = educationUtils.getStudentId(request, courseId);
        Task task = taskRepository.findById(taskId).get();

        Path path = Paths.get(task.getReference());
        return new UrlResource(path.toUri());

    }

    public Resource downloadPassedTask(HttpServletRequest request,
                                       Long courseId,
                                       Long taskId) throws MalformedURLException {
        Long studentId = educationUtils.getStudentId(request, courseId);

        List<Long> studentCourseIdList = studentRepository.findStudentsIdByCourse(courseId);
//
        for (Long studentCourseId : studentCourseIdList) {
            if (studentCourseId.equals(studentId)) {
                String passedTaskReference = passedTaskRepository
                        .findReferenceByStudentIdAndCourseIdAndTaskId(
                                studentId,
                                courseId,
                                taskId);
                if (passedTaskReference.isEmpty())
                    throw new NoPassedTaskException("No such passed task with task id = " + taskId
                            + " on course with id = " + courseId + " and student id = " + studentId);
                Path path = Paths.get(passedTaskReference);
                return new UrlResource(path.toUri());
            }
        }
        throw new NoSuchStudentOnCourseException("There is no student with id = " + studentId +
                " on course with id = " + courseId);
    }

    public Student createProfile(HttpServletRequest request, Student student) {
        UserEntity user = jwtService.getUserByJwt(request);
        student.setUser(user);
        student.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));
        studentRepository.save(student);
        return student;
    }

    public CommonResponse joinCourse(HttpServletRequest request, Long courseId) {
        Long studentId = jwtService.getStudentId(request);
        Student student = studentRepository.findById(studentId).get();
        Course course = courseRepository.findById(courseId).get();

        student.addCourse(course);
        fileService.createStudentDirectories(course, student);

        studentRepository.save(student);
        return new CommonResponse(true);
    }

    public CommonResponse deletePassedTask(HttpServletRequest request,
                                           Long courseId, Long taskId) {
        Long studentId = educationUtils.getStudentId(request, courseId);
        PassedTask passedTask = passedTaskRepository.
                findByCourseIdStudentIdTaskId(courseId, studentId, taskId);
        if (passedTask == null)
            throw new NoPassedTaskException("No such passed task with id = " + taskId
                    + " on course with id = " + courseId);
        File file = new File(passedTask.getReference());
        if (file.exists())
            file.delete();
        passedTaskRepository.delete(passedTask);
        return new CommonResponse(true);
    }

    public CommonResponse deletePassedTaskFile(HttpServletRequest request,
                                               Long courseId, Long taskId) {
        Long studentId = educationUtils.getStudentId(request, courseId);
        PassedTask passedTask = passedTaskRepository.
                findByCourseIdStudentIdTaskId(courseId, studentId, taskId);
        if (passedTask == null)
            throw new NoPassedTaskException("No such passed task with id = " + taskId
                    + " on course with id = " + courseId);
        File file = new File(passedTask.getReference());
        if (file.exists())
            file.delete();
        passedTask.setReference(null);
        passedTaskRepository.save(passedTask);
        return new CommonResponse(true);
    }

    public List<TaskDTO> getCourseTasks(HttpServletRequest request, Long courseId) {
        Long studentId = educationUtils.getStudentId(request, courseId);
        return taskRepository.findFullTaskSInfoByCourseId(courseId);
    }

    public CommonResponse editStudentProfile(HttpServletRequest request, Student updatedStudent) {
        Long studentId = jwtService.getStudentId(request);
        Student student = studentRepository.findById(studentId).get();
        student.updateStudent(updatedStudent);
        studentRepository.save(student);
        return new CommonResponse(true);
    }

    public CommonResponse editPassedTask(HttpServletRequest request, Long courseId,
                                         Long taskId,
                                         PassedTask updatedPassedTask) {
        Long studentId = educationUtils.getStudentId(request, courseId);
        PassedTask passedTask = passedTaskRepository
                .findByCourseIdStudentIdTaskId(courseId, studentId, taskId);
        passedTask.updatePassedTaskStudent(updatedPassedTask);
        passedTaskRepository.save(passedTask);
        return new CommonResponse(true);
    }

    public Object createRepo(HttpServletRequest request, Long courseId, Long taskId, Object repo) throws JsonProcessingException {
        Long studentId = educationUtils.getStudentId(request, courseId);
        Student student = studentRepository.findById(studentId).get();
        PassedTask passedTask = passedTaskRepository.findByCourseIdStudentIdTaskId(courseId, studentId, taskId);
        Map<String, String> githubReference = (Map<String, String>) githubFeign.createRepo(student.getGithubAccessToken(), repo);
        if (passedTask != null) {
            passedTask.setGithubReference(githubReference.get("html_url"));

            passedTaskRepository.save(passedTask);
            return githubReference;
        } else {
            PassedTask newPassedTask = new PassedTask();
            newPassedTask.setGithubReference(githubReference.get("html_url"));
            newPassedTask.setStudent(student);
            newPassedTask.setTask(taskRepository.findById(taskId).get());

            passedTaskRepository.save(newPassedTask);
            return githubReference;
        }
    }

    public Object importRepo(HttpServletRequest request, Long courseId, Long taskId) {
        Long studentId = educationUtils.getStudentId(request, courseId);
        Student student = studentRepository.findById(studentId).get();
        Task task = taskRepository.findById(taskId).get();
        PassedTask passedTask = passedTaskRepository.findByCourseIdStudentIdTaskId(courseId, studentId, taskId);

        String githubReference = passedTask.getGithubReference();

        String[] parts = githubReference.split("/");
        String username = parts[parts.length - 2];
        String repo = parts[parts.length - 1];

        RepositoryImportRequest importRequest = new RepositoryImportRequest(task.getGithubReference(), username, repo);
        Map<String, String> githubHtmlReference = (Map<String, String>) githubFeign.importRepo(student.getGithubAccessToken(),
                importRequest);

        return githubHtmlReference;
    }

    public Map<String, String> provideAccessToGithub(HttpServletRequest request) {
        Long studentId = jwtService.getStudentId(request);
        String url = "http://25.59.188.46:8081/api/v1/oauth2/authorize";
        Map<String, String> oauth2url = new HashMap<>();
        oauth2url.put("auth", url);
        return oauth2url;
    }

    public CommonResponse saveAccessToken(HttpServletRequest request, GithubAccessToken githubAccessToken) {
        Long studentId = jwtService.getStudentId(request);
        Student student = studentRepository.findById(studentId).get();
        student.setGithubAccessToken(githubAccessToken.getGithubAccessToken());
        studentRepository.save(student);
        return new CommonResponse(true);
    }

    public EducatorDTO getEducatorProfile(HttpServletRequest request, Long courseId) {
        Long studentId = educationUtils.getStudentId(request, courseId);
        return educatorRepository.findEducatorPersonal(courseId);
    }

    public List<FileToCheck> getFileToCheck(HttpServletRequest request, Long courseId,
                                            Long taskId) {
        Long studentId = educationUtils.getStudentId(request, courseId);
        Task task = taskRepository.findById(taskId).get();
        return task.getFilesToCheck();
    }
}
