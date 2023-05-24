package com.unforgettable.securitypart.service;

import com.unforgettable.securitypart.entity.*;
import com.unforgettable.securitypart.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final CourseRepository courseRepository;
    private final EducatorRepository educatorRepository;
    private final StudentRepository studentRepository;
    private final PassedTaskRepository passedTaskRepository;
    private final TaskRepository taskRepository;

    public FileService(CourseRepository courseRepository,
                       EducatorRepository educatorRepository,
                       StudentRepository studentRepository,
                       PassedTaskRepository passedTaskRepository,
                       TaskRepository taskRepository) {
        this.courseRepository = courseRepository;
        this.educatorRepository = educatorRepository;
        this.studentRepository = studentRepository;
        this.passedTaskRepository = passedTaskRepository;
        this.taskRepository = taskRepository;
    }

    public void uploadFile(MultipartFile file) throws IOException {
        file.transferTo(new File("D:\\Files\\" + file.getOriginalFilename()));
    }

    public void createEducatorDirectory(Educator educator){
        File educatorsDir = new File("D:\\Files\\");
        if (!educatorsDir.exists()) {
            educatorsDir.mkdirs();
        }

        File educatorDir = new File(educatorsDir, educator.getFirstname() + educator.getLastname());
        if (!educatorDir.exists()) {
            educatorDir.mkdirs();
        }
    }

    public void createCourseDirectory(Course course, Educator educator){
        File parentDir = new File("D:\\Files\\"+educator.getFirstname() + educator.getLastname());

        File courseDir = new File(parentDir, course.getTitle());
        if (!courseDir.exists()) {
            courseDir.mkdirs();
        }

        File taskFile = new File(courseDir, "Tasks");
        if(!taskFile.exists()){
            taskFile.mkdirs();
        }

        File studentFile = new File(courseDir, "Students");
        if(!studentFile.exists())
            studentFile.mkdirs();

    }

    public void createStudentDirectories(Course course, Student student){
        String parentDir = "D:\\Files\\" + course.getEducator().getFirstname() + course.getEducator().getLastname() +
                "\\" + course.getTitle() +
                "\\Students\\";
        File studentDir = new File(parentDir, student.getFirstname() + student.getLastname());
        if (!studentDir.exists()) {
            studentDir.mkdirs();
        }

        File labsDir = new File(studentDir, "Labs");
        if (!labsDir.exists()) {
            labsDir.mkdirs();
        }
    }
//    public void createDirectories() {
//        File educatorsDir = new File("D:\\Files\\");
//        if (!educatorsDir.exists()) {
//            educatorsDir.mkdirs();
//        }
//
//        List<Educator> educators = educatorRepository.findAll();
//
//        for (Educator educator : educators) {
//            File educatorDir = new File(educatorsDir, educator.getFirstname() + educator.getLastname());
//            if (!educatorDir.exists()) {
//                educatorDir.mkdirs();
//            }
//
//            List<Course> courses = educator.getCourses();
//            for (Course course : courses) {
//                File courseDir = new File(educatorDir, course.getTitle());
//                if (!courseDir.exists()) {
//                    courseDir.mkdirs();
//                }
//
//                File taskFile = new File(courseDir, "Tasks");
//                if(!taskFile.exists()){
//                    taskFile.mkdirs();
//                }
//
//                File studentFile = new File(courseDir, "Students");
//                if(!studentFile.exists())
//                    studentFile.mkdirs();
//
//                List<Student> students = studentRepository.findStudentsByCourseId(course.getId());
//                for (Student student : students) {
//                    File studentDir = new File(studentFile, student.getFirstname() + student.getLastname());
//                    if (!studentDir.exists()) {
//                        studentDir.mkdirs();
//                    }
//
//                    File labsDir = new File(studentDir, "Labs");
//                    if (!labsDir.exists()) {
//                        labsDir.mkdirs();
//                    }
//
//                }
//            }
//        }
}

