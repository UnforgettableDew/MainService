package com.unforgettable.securitypart.repository;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.StudentDTO;
import com.unforgettable.securitypart.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select s.id from Course c join c.students s where c.id=:courseId")
    List<Long> findStudentsIdByCourse(Long courseId);

    @Query("select s.id from Student s where s.user.id=:userId")
    Long findStudentIdByUserId(Long userId);

    @Query("select new com.unforgettable.securitypart.dto.StudentDTO(" +
            "s.id, s.firstname, s.lastname, s.group) from Student s " +
            "join s.courses c where c.id=:courseId and c.educator.id=:educatorId")
    List<StudentDTO> findStudentsByCourseIdAndEducatorId(Long courseId, Long educatorId);

    @Query("select new com.unforgettable.securitypart.dto.StudentDTO(" +
            "s.id, s.firstname, s.lastname, s.group) " +
            "from Student s join s.passedTasks lw " +
            "where lw.task.id=:taskId and lw.task.course.id=:courseId")
    List<StudentDTO> findStudentsWhoPassedTask(Long courseId, Long taskId);

    @Query("select distinct new com.unforgettable.securitypart.dto.StudentDTO(" +
            "s.id, s.firstname, s.lastname, s.group) from Student s " +
            "join s.passedTasks lw " +
            "where lw.isAssessed=false and lw.task.course.id=:courseId")
    List<StudentDTO> findStudentWithUncheckedLabs(Long courseId);

    @Query("select new com.unforgettable.securitypart.dto.StudentDTO(" +
            "s.id, s.firstname, s.lastname, s.group) " +
            "from Student s join s.passedTasks pt " +
            "where pt.task.id=:taskId and pt.task.course.id=:courseId and pt.submissionDate>pt.task.endDate")
    List<StudentDTO> getStudentsWhoDidntSubmitTaskOnTime(Long courseId, Long taskId);

    @Query("select new com.unforgettable.securitypart.dto.StudentDTO(" +
            "s.id, s.firstname, s.lastname, s.group, s.email, s.birthday, s.registrationDate, s.telegramContact)" +
            " from Student s join s.courses c" +
            " where s.id=:studentId and c.id=:courseId and c.educator.id=:educatorId")
    StudentDTO findStudentByIdAndCourseId(Long studentId, Long courseId, Long educatorId);

    @Query("select new com.unforgettable.securitypart.dto.StudentDTO(" +
            "s.id, s.firstname, s.lastname, s.group, s.email, s.birthday, s.registrationDate, s.telegramContact)" +
            " from Student s " +
            " where s.id=:studentId")
    StudentDTO findStudentProfileById(Long studentId);

    @Query("select new com.unforgettable.securitypart.dto.StudentDTO(" +
            "s.id, s.firstname, s.lastname, s.group)" +
            " from Student s " +
            " where s.id=:studentId")
    StudentDTO findStudentBriefInfo(Long studentId);

    @Query("select s from Student s join s.courses c where c.id=:courseId")
    List<Student> findStudentsByCourseId(Long courseId);


    @Query("select count(s) from Student s join s.courses c " +
            "where c.id=:courseId")
    Integer countStudentsByCourseId(Long courseId);

    @Query("select count(s) from Student s join s.passedTasks lw " +
            "where lw.task.id=:taskId")
    Integer countStudentsByTaskId(Long taskId);
}
