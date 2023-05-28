package com.unforgettable.securitypart.repository;

import com.unforgettable.securitypart.entity.FileToCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileToCheckRepository extends JpaRepository<FileToCheck, Long> {

}
