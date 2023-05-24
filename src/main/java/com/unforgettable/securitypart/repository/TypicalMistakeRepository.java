package com.unforgettable.securitypart.repository;

import com.unforgettable.securitypart.dto.TypicalMistakeDTO;
import com.unforgettable.securitypart.entity.TypicalMistake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypicalMistakeRepository extends JpaRepository<TypicalMistake, Long> {

    @Query("select new com.unforgettable.securitypart.dto.TypicalMistakeDTO(" +
            "tm.id, tm.title, tm.description, tm.deductedPoint) from TypicalMistake tm " +
            "where tm.course.id=:courseId")
    List<TypicalMistakeDTO> findTypicalMistakesByCourseId(Long courseId);

    @Query("select new com.unforgettable.securitypart.dto.TypicalMistakeDTO(" +
            "tm.id, tm.title, tm.description, tm.deductedPoint) from TypicalMistake tm " +
            "where tm.course.id=:courseId and tm.id=:typicalMistakeId")
    TypicalMistakeDTO findByCourseIdAndTypicalMistakeId(Long courseId, Long typicalMistakeId);
}
