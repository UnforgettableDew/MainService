package com.unforgettable.securitypart.repository;

import com.unforgettable.securitypart.dto.TaskDTO;
import com.unforgettable.securitypart.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("select new com.unforgettable.securitypart.dto.TaskDTO" +
            "(t.id, t.title, t.description, t.maxPoint, t.startDate, t.endDate) from Task t " +
            "where t.id=:taskId")
    TaskDTO findFullTaskInfoById(Long taskId);

    @Query("select new com.unforgettable.securitypart.dto.TaskDTO" +
            "(t.id, t.title, t.description, t.maxPoint, t.startDate, t.endDate) from Task t " +
            "where t.course.id=:courseId")
    List<TaskDTO> findFullTaskSInfoByCourseId(Long courseId);

    @Query("select new com.unforgettable.securitypart.dto.TaskDTO" +
            "(t.id, t.title, t.maxPoint) from Task t " +
            "where t.id=:taskId")
    TaskDTO findTaskById(Long taskId);

    @Query("select new com.unforgettable.securitypart.dto.TaskDTO" +
            "(t.id, t.title, t.maxPoint) from Task t " +
            "join t.passedTasks pt where pt.id=:passedTaskId")
    TaskDTO findTaskByPassedTaskId(Long passedTaskId);

//    @Query("select count(c.tasks) from Course c where c.id=:courseId")
    Integer countTaskByCourseId(Long courseId);

    @Query("select t.maxPoint from Task t join t.passedTasks lw" +
            " where lw.id=:passedTaskId")
    Float scoreByLaboratoryWorkId(Long passedTaskId);

    @Query("select new com.unforgettable.securitypart.dto.TaskDTO(t.id, t.title, t.maxPoint)" +
            " from Task t where t.course.id=:courseId")
    List<TaskDTO> findTasksByCourseId(Long courseId);
}
