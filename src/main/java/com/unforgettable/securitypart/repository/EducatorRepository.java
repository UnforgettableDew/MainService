package com.unforgettable.securitypart.repository;

import com.unforgettable.securitypart.dto.EducatorDTO;
import com.unforgettable.securitypart.entity.Educator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducatorRepository extends JpaRepository<Educator, Long> {
    @Query("select c.educator.id from Course c where c.id=:courseId")
    Long findEducatorIdByCourse(Long courseId);

    @Query("select e.id from Educator e where e.user.id=:userId")
    Long findEducatorIdByUserId(Long userId);

    @Query("select c.educator.firstname, c.educator.lastname from Course c where c.id=:courseId")
    String findEducatorNameByCourseId(Long courseId);

    @Query("select new com.unforgettable.securitypart.dto.EducatorDTO(e.firstname, e.lastname, e.email,e.birthday, e.telegramContact)" +
            " from Educator e join e.courses c where c.id=:courseId")
    EducatorDTO findEducatorPersonal(Long courseId);
}
