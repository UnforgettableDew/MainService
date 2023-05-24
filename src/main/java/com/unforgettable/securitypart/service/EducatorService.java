package com.unforgettable.securitypart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unforgettable.securitypart.dto.*;
import com.unforgettable.securitypart.entity.*;
import com.unforgettable.securitypart.exception.NoPassedTaskException;
import com.unforgettable.securitypart.exception.NoSuchStudentOnCourseException;
import com.unforgettable.securitypart.feign.GithubFeign;
import com.unforgettable.securitypart.model.request.AssessRequest;
import com.unforgettable.securitypart.model.request.GithubAccessToken;
import com.unforgettable.securitypart.model.response.CommonResponse;
import com.unforgettable.securitypart.model.response.GithubHtmlUrlResponse;
import com.unforgettable.securitypart.repository.*;
import com.unforgettable.securitypart.utils.EducationUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class EducatorService {
    private final CourseRepository courseRepository;
    private final EducatorRepository educatorRepository;
    private final StudentRepository studentRepository;
    private final PassedTaskRepository passedTaskRepository;
    private final TaskRepository taskRepository;
    private final TypicalMistakeRepository typicalMistakeRepository;
    private final JwtService jwtService;
    private final GithubFeign githubFeign;
    private final EducationUtils educationUtils;

    private final FileService fileService;

    @Autowired
    public EducatorService(CourseRepository courseRepository,
                           EducatorRepository educatorRepository,
                           StudentRepository studentRepository,
                           PassedTaskRepository passedTaskRepository,
                           TaskRepository taskRepository,
                           TypicalMistakeRepository typicalMistakeRepository,
                           JwtService jwtService,
                           GithubFeign githubFeign,
                           EducationUtils educationUtils, FileService fileService) {
        this.courseRepository = courseRepository;
        this.educatorRepository = educatorRepository;
        this.studentRepository = studentRepository;
        this.passedTaskRepository = passedTaskRepository;
        this.taskRepository = taskRepository;
        this.typicalMistakeRepository = typicalMistakeRepository;
        this.jwtService = jwtService;
        this.githubFeign = githubFeign;
        this.educationUtils = educationUtils;
        this.fileService = fileService;
    }

//    private Long getEducatorId(HttpServletRequest request, Long courseId) {
//        Long educatorId = jwtService.getEducatorId(request);
//
//        Long educatorCourseId = educatorRepository.findEducatorIdByCourse(courseId);
//        if (!educatorCourseId.equals(educatorId))
//            throw new CourseDoesntBelongEducatorException("Course with id = " + courseId
//                    + " does not belong educator with id = " + educatorId);
//        return educatorId;
//    }
//
//    private Long getCourseId(Long courseId ,Long taskId){
//        Long courseIdByTaskId = courseRepository.findCourseIdByTaskId(taskId);
//        if(!courseId.equals(courseIdByTaskId))
//            throw new TaskDoesntBelongCourseException("Task with id = " + taskId +
//                    " does not belong course with id = " + courseId);
//        return courseIdByTaskId;
//    }

    public List<CourseDTO> getEducatorCourses(HttpServletRequest request) {
        Long educatorId = jwtService.getEducatorId(request);

        return courseRepository.findCoursesByEducatorId(educatorId);
    }

    public CourseDTO getEducatorCourse(HttpServletRequest request, Long courseId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        return new CourseDTO(courseRepository.findCourseByIdAndEducatorId(courseId, educatorId));
    }

    public List<TypicalMistakeDTO> getCourseTypicalMistakes(HttpServletRequest request,
                                                            Long courseId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);
        return typicalMistakeRepository.findTypicalMistakesByCourseId(courseId);
    }

    public List<StudentDTO> getListOfStudentsByCourse(HttpServletRequest request, Long courseId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        List<StudentDTO> students = studentRepository.findStudentsByCourseIdAndEducatorId(courseId, educatorId);

        for (StudentDTO student : students) {
            List<PassedTaskDTO> passedTasks = passedTaskRepository.
                    findPassedTasksByStudentIdAndCourseId(student.getId(), courseId);

            passedTasks.forEach(passedTaskDTO ->
                    passedTaskDTO.setTask(taskRepository.
                            findTaskByPassedTaskId(passedTaskDTO.getId())));

            student.setPassedTasks(passedTasks);
        }
        return students;
    }

    public StudentDTO getStudentProfile(HttpServletRequest request,
                                        Long courseId,
                                        Long studentId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        StudentDTO student = studentRepository.findStudentByIdAndCourseId(studentId, courseId, educatorId);

        if (student == null)
            throw new NoSuchStudentOnCourseException("There is no student with id = " + studentId +
                    " on course with id = " + courseId);

        List<PassedTaskDTO> passedTasks = passedTaskRepository.
                findPassedTasksByStudentIdAndCourseId(studentId, courseId);

        passedTasks.forEach(passedTaskDTO ->
                passedTaskDTO.setTask(taskRepository.
                        findTaskByPassedTaskId(passedTaskDTO.getId())));
        student.setPassedTasks(passedTasks);
        return student;
    }

    public List<StudentDTO> getStudentsWhoPassedTask(HttpServletRequest request,
                                                     Long courseId,
                                                     Long taskId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        Long checkedCourseId = educationUtils.getCourseId(courseId, taskId);

        List<StudentDTO> students = studentRepository.findStudentsWhoPassedTask(courseId, taskId);
        for (StudentDTO student : students) {
            PassedTaskDTO passedTask = passedTaskRepository
                    .findPassedTaskByStudentIdCourseIdTaskId(student.getId(), courseId, taskId);

            passedTask.setTask(taskRepository
                    .findTaskByPassedTaskId(passedTask.getId()));

            student.addPassedTask(passedTask);
        }
        return students;
    }

    public TaskDTO getTaskByCourse(HttpServletRequest request,
                                   Long courseId, Long taskId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);
        return taskRepository.findFullTaskInfoById(taskId);
    }

    public PassedTaskDTO getPassedTaskByCourseAndStudent(HttpServletRequest request,
                                                         Long courseId,
                                                         Long studentId,
                                                         Long passedTaskId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        List<Long> studentCourseIdList = studentRepository.findStudentsIdByCourse(courseId);
//
        for (Long studentCourseId : studentCourseIdList) {
            if (studentCourseId.equals(studentId)) {
                PassedTaskDTO passedTask = passedTaskRepository
                        .findPassedTasksByStudentIdAndCourseIdAndTaskId(
                                studentId,
                                courseId,
                                passedTaskId);
                if (passedTask == null)
                    throw new NoPassedTaskException("No such passed task with task id = " + passedTaskId
                            + " on course with id = " + courseId + " and student id = " + studentId);
                passedTask.setTask(taskRepository.findFullTaskInfoById(passedTaskId));
                return passedTask;
            }
        }
        throw new NoSuchStudentOnCourseException("There is no student with id = " + studentId +
                " on course with id = " + courseId);
    }

    public Map<String, Object> courseStats(HttpServletRequest request, Long courseId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        Map<String, Object> stats = new HashMap<>();
        Integer studentsCount = studentRepository.countStudentsByCourseId(courseId);
        Integer tasksCount = taskRepository.countTaskByCourseId(courseId);

        List<TaskDTO> tasks = taskRepository.findTasksByCourseId(courseId);
        List<PassedTaskDTO> passedTasks = new ArrayList<>();

        for (TaskDTO task : tasks) {
            PassedTaskDTO passedTask = new PassedTaskDTO(passedTaskRepository
                    .avgScoreForLWByCourseAndTask(courseId, task.getId()));
            passedTask.setTask(task);
            passedTask.setStudentsCount(studentRepository.countStudentsByTaskId(task.getId()));
            passedTasks.add(passedTask);
        }

        stats.put("total_tasks_count", studentsCount * tasksCount);
        stats.put("passed_tasks", passedTasks);
        return stats;
    }

    public List<StudentDTO> getStudentsWithUncheckedPassedTasks(HttpServletRequest request,
                                                                Long courseId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        List<StudentDTO> students = studentRepository.findStudentWithUncheckedLabs(courseId);

        for (StudentDTO student : students) {
            List<PassedTaskDTO> passedTasks = passedTaskRepository.
                    findUncheckedLWByStudentAndCourse(student.getId(), courseId);

            passedTasks.forEach(passedTaskDTO ->
                    passedTaskDTO.setTask(taskRepository.
                            findTaskByPassedTaskId(passedTaskDTO.getId())));
            student.setPassedTasks(passedTasks);
        }

        return students;
    }

    public List<Object> getCommitList(HttpServletRequest request,
                                      Long courseId, Long studentId,
                                      Long passedTaskId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

//        List<Long> studentCourseIdList = studentRepository.findStudentsIdByCourse(courseId);
//
//        for (Long studentCourseId : studentCourseIdList) {
//            if (studentCourseId.equals(studentId)) {
        String githubReference = passedTaskRepository.findGithubReferenceByPassedTaskIdCourseIdStudentId(passedTaskId, courseId, studentId);
        if (githubReference == null)
            throw new NoPassedTaskException("No such passed task with id = " + passedTaskId
                    + " on course with id = " + courseId);
        String[] parts = githubReference.split("/");
        String username = parts[parts.length - 2];
        String repo = parts[parts.length - 1];
        return githubFeign.getAllCommits(username, repo);
//            }
//        }
//        return null;
    }

    public Course createCourse(HttpServletRequest request, Course course) {
        Long educatorId = jwtService.getEducatorId(request);
        Educator educator = educatorRepository.findById(educatorId).get();
        course.setEducator(educator);
        courseRepository.save(course);

        fileService.createCourseDirectory(course, educator);
        return course;
    }

    public Task addTask(HttpServletRequest request, Long courseId, Task task) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);
        Educator educator = educatorRepository.findById(educatorId).get();
        Course course = courseRepository.findById(courseId).get();
        task.setCourse(course);

        taskRepository.save(task);
        return task;
    }

    public Educator createProfile(HttpServletRequest request, Educator educator) {
        UserEntity user = jwtService.getUserByJwt(request);
        educator.setUser(user);

        educatorRepository.save(educator);

        fileService.createEducatorDirectory(educator);
        return educator;
    }

    public List<StudentDTO> getStudentsWhoDidntSubmitTaskOnTime(HttpServletRequest request,
                                                                Long courseId, Long taskId){
        Long educatorId = educationUtils.getEducatorId(request, courseId);
        List<StudentDTO> students = studentRepository.getStudentsWhoDidntSubmitTaskOnTime(courseId, taskId);

        for (StudentDTO student : students) {
            List<PassedTaskDTO> passedTasks = passedTaskRepository
                    .findPassedTasksByStudentIdCourseIdTaskId(student.getId(), courseId, taskId);

            passedTasks.forEach(passedTaskDTO ->
                    passedTaskDTO.setTask(taskRepository.
                            findTaskByPassedTaskId(passedTaskDTO.getId())));
            student.setPassedTasks(passedTasks);
        }

        return students;
    }

    public List<Object> checkFileExistence(HttpServletRequest request, Long courseId,
                                           Long studentId, Long passedTaskId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);
        List<Long> studentCourseIdList = studentRepository.findStudentsIdByCourse(courseId);

        for (Long studentCourseId : studentCourseIdList) {
            if (studentCourseId.equals(studentId)) {
                String githubReference = passedTaskRepository
                        .findGithubReferenceByPassedTaskIdCourseIdStudentId(passedTaskId, courseId, studentId);

                if (githubReference == null)
                    throw new NoPassedTaskException("No such passed task with id = " + passedTaskId
                            + " on course with id = " + courseId + " and student id = " + studentId);

                String[] parts = githubReference.split("/");
                String username = parts[parts.length - 2];
                String repo = parts[parts.length - 1];
                return githubFeign.getFiles(username, repo);
            }
        }
        throw new NoSuchStudentOnCourseException("There is no student with id = " + studentId +
                " on course with id = " + courseId);
    }

    public Educator getEducatorProfile(HttpServletRequest request) {
        Long educatorId = jwtService.getEducatorId(request);

        return educatorRepository.findById(educatorId)
                .orElseThrow();
    }

    public Resource downloadPassedTask(HttpServletRequest request,
                                       Long courseId,
                                       Long studentId,
                                       Long taskId) throws MalformedURLException {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

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

    public CommonResponse uploadTask(HttpServletRequest request,
                                     Long courseId,
                                     Long taskId,
                                     MultipartFile file) throws IOException {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        Course course = courseRepository.findById(courseId).get();
        Task task = taskRepository.findById(taskId).get();
        String educatorName = educatorRepository.findEducatorNameByCourseId(courseId).replace(",", "");

        String path = "D:\\Files\\" + educatorName +
                "\\" + course.getTitle() +
                "\\Tasks\\";
        task.setReference(path + file.getOriginalFilename());
        Path filePath = Paths.get(path, file.getOriginalFilename());
        file.transferTo(filePath.toFile());
        taskRepository.save(task);
        return new CommonResponse(true);
    }

    public CommonResponse addTypicalMistake(HttpServletRequest request,
                                            Long courseId,
                                            TypicalMistake typicalMistake) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);
        Course course = courseRepository.findById(courseId).get();
        typicalMistake.setCourse(course);
        typicalMistakeRepository.save(typicalMistake);
        return new CommonResponse(true);
    }

    public PassedTaskDTO assessStudentPassedTask(HttpServletRequest request,
                                                 Long courseId,
                                                 Long studentId,
                                                 Long passedTaskId,
                                                 AssessRequest assess) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        List<Long> studentCourseIdList = studentRepository.findStudentsIdByCourse(courseId);
//
        for (Long studentCourseId : studentCourseIdList) {
            if (studentCourseId.equals(studentId)) {
                PassedTask passedTask = passedTaskRepository
                        .findByCourseIdStudentIdTaskId(courseId, studentId, passedTaskId);

                if (passedTask == null)
                    throw new NoPassedTaskException("No such passed task with id = " + passedTaskId
                            + " on course with id = " + courseId + " and student id = " + studentId);


                passedTask.setIsAssessed(true);
                passedTask.setEducatorComment(assess.getComment());
                passedTask.setPoint(assess.getPoint());
                passedTaskRepository.save(passedTask);
                return new PassedTaskDTO(passedTask);
            }
        }
        throw new NoSuchStudentOnCourseException("There is no student with id = " + studentId +
                " on course with id = " + courseId);
    }

    public CommonResponse deleteTypicalMistake(HttpServletRequest request,
                                               Long courseId,
                                               Long typicalMistakeId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        typicalMistakeRepository.deleteById(typicalMistakeId);
        return new CommonResponse(true);
    }

    public CommonResponse deleteTask(HttpServletRequest request,
                                     Long courseId,
                                     Long taskId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        taskRepository.deleteById(taskId);
        return new CommonResponse(true);
    }

    public CommonResponse kickStudent(HttpServletRequest request,
                                      Long courseId,
                                      Long studentId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);
        List<Long> studentCourseIdList = studentRepository.findStudentsIdByCourse(courseId);

        for (Long studentCourseId : studentCourseIdList) {
            if (studentCourseId.equals(studentId)) {

                studentRepository.deleteById(studentId);
                return new CommonResponse(true);
            }
        }
        throw new NoSuchStudentOnCourseException("There is no student with id = " + studentId +
                " on course with id = " + courseId);
    }

    public CommonResponse deleteCourse(HttpServletRequest request,
                                       Long courseId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);
        courseRepository.deleteById(courseId);
        return new CommonResponse(true);
    }

    public List<TaskDTO> getCourseTasks(HttpServletRequest request, Long courseId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);
        return taskRepository.findFullTaskSInfoByCourseId(courseId);
    }

    public CommonResponse editProfile(HttpServletRequest request, Educator updatedEducator) {
        Long educatorId = jwtService.getEducatorId(request);
        Educator educator = educatorRepository.findById(educatorId).get();
        educator.updateEducator(updatedEducator);
        educatorRepository.save(educator);

        return new CommonResponse(true);
    }

    public CommonResponse editCourse(HttpServletRequest request,
                                     Long courseId, Course updatedCourse) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);
        Course course = courseRepository.findById(courseId).get();

        course.updateCourse(updatedCourse);

        courseRepository.save(course);
        return new CommonResponse(true);
    }

    public CommonResponse editTask(HttpServletRequest request, Long courseId,
                                   Long taskId, Task updatedTask) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        Task task = taskRepository.findById(taskId).get();
        task.updateTask(updatedTask);
        taskRepository.save(task);
        return new CommonResponse(true);
    }

    public CommonResponse editPassedTask(HttpServletRequest request, Long courseId,
                                         Long taskId, Long studentId,
                                         PassedTask updatedPassedTask) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);
        PassedTask passedTask = passedTaskRepository
                .findByCourseIdStudentIdTaskId(courseId, studentId, taskId);

        passedTask.updatePassedTaskEducator(updatedPassedTask);
        passedTaskRepository.save(passedTask);
        return new CommonResponse(true);
    }

    public CommonResponse editTypicalMistake(HttpServletRequest request, Long courseId,
                                             Long typicalMistakeId,
                                             TypicalMistake updatedTypicalMistake) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);
        TypicalMistake typicalMistake = typicalMistakeRepository.findById(typicalMistakeId).get();
        typicalMistake.updateTypicalMistake(updatedTypicalMistake);
        typicalMistakeRepository.save(typicalMistake);
        return new CommonResponse(true);
    }

    public Object createRepo(HttpServletRequest request, Long courseId, Long taskId, Object repo) throws JsonProcessingException {
        Long educatorId = educationUtils.getEducatorId(request, courseId);
        Educator educator = educatorRepository.findById(educatorId).get();
        Map<String, String> githubReference = (Map<String, String>) githubFeign.createRepo(educator.getGithubAccessToken(), repo);
        Task task = taskRepository.findById(taskId).get();

        task.setGithubReference(githubReference.get("html_url"));
        taskRepository.save(task);
        return githubReference;
    }

    public Map<String, String> provideAccessToGithub(HttpServletRequest request){
        Long educatorId = jwtService.getEducatorId(request);
        String url = "http://25.59.188.46:8081/api/v1/oauth2/authorize";
        Map<String, String> oauth2url = new HashMap<>();
        oauth2url.put("auth", url);
        return oauth2url;
    }

    public CommonResponse saveAccessToken(HttpServletRequest request, GithubAccessToken githubAccessToken){
        Long educatorId = jwtService.getEducatorId(request);
        Educator educator = educatorRepository.findById(educatorId).get();
        educator.setGithubAccessToken(githubAccessToken.getGithubAccessToken());
        educatorRepository.save(educator);
        return new CommonResponse(true);
    }
}

