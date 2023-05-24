package com.unforgettable.securitypart.repository;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select new com.unforgettable.securitypart.dto.CourseDTO(" +
            "c.id, c.title, c.description, c.startDate, c.endDate) from Course c " +
            "where c.educator.id=:id")
    List<CourseDTO> findCoursesByEducatorId(Long id);

    Course findCourseByIdAndEducatorId(Long courseId, Long educatorId);

    @Query("select c from Course c join c.students s where s.id=:studentId and c.id=:courseId")
    Course findCourseByStudentId(Long studentId, Long courseId);
    @Query("select c.title from Course c where c.id=:courseId")
    String findCourseTitleById(Long courseId);

    @Query("select new com.unforgettable.securitypart.dto.CourseDTO(" +
            "c.id, c.title, c.description, c.startDate, c.endDate) from Course c " +
            "join c.students s where s.id=:id")
    List<CourseDTO> findCoursesByStudentId(Long id);

    @Query("select t.course.id from Task t where t.id=:taskId")
    Long findCourseIdByTaskId(Long taskId);
//    @Query("select new com.unforgettable.securitypart.dto.StudentDTO(" +
//            "s.id, s.firstname, s.lastname, s.group) from Student s " +
//            "join s.courses c where c.id=:courseId and c.educator.id=:educatorId")
//    List<StudentDTO> findStudentsByCourseIdAndEducatorId(Long courseId, Long educatorId);
//    @Query("select new com.unforgettable.securitypart.dto.StudentDTO(" +
//            "s.id, s.firstname, s.lastname, s.group) " +
//            "from Student s join s.laboratoryWorks lw " +
//            "where lw.task.id=:taskId and lw.task.course.id=:courseId")
//    List<StudentDTO> findStudentsWhoPassedTask(Long courseId, Long taskId);
//    @Query("select new com.unforgettable.securitypart.dto.StudentDTO(" +
//            "s.id, s.firstname, s.lastname, s.group, s.email, s.age, s.registrationDate, s.telegramContact)" +
//            " from Student s join s.courses c" +
//            " where s.id=:studentId and c.id=:courseId and c.educator.id=:educatorId")
//    StudentDTO findStudentByIdAndCourseId(Long studentId, Long courseId, Long educatorId);
//    @Query("select new com.unforgettable.securitypart.dto.LaboratoryWorkDTO(" +
//            "lw.id, lw.score, lw.isPassed) from LaboratoryWork lw " +
//            "where lw.student.id=:studentId and lw.task.course.id=:courseId")
//    List<LaboratoryWorkDTO> findLaboratoryWorksByStudentIdAndCourseId(Long studentId, Long courseId);
//    @Query("select new com.unforgettable.securitypart.dto.LaboratoryWorkDTO(" +
//            "lw.id, lw.score, lw.isPassed, lw.githubReference) from LaboratoryWork lw " +
//            "join lw.task t where lw.student.id=:studentId and t.id=:taskId and t.course.id=:courseId")
//    LaboratoryWorkDTO findLaboratoryWorkByStudentIdCourseIdTaskId(Long studentId, Long courseId, Long taskId);
//    @Query("select new com.unforgettable.securitypart.dto.TaskDTO(" +
//            "t.id, t.maxScore) from Task t " +
//            "join t.laboratoryWorks lw where lw.id=:laboratoryWorkId")
//    TaskDTO findTaskByLaboratoryWorkId(Long laboratoryWorkId);

//    @Query("select c.educator.id from Course c where c.id=:courseId")
//    Long findEducatorIdByCourse(Long courseId);

}
