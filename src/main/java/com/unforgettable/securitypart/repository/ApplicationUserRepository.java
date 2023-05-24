package com.unforgettable.securitypart.repository;

import com.unforgettable.securitypart.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("select u.id from UserEntity u where u.username=:username")
    Long findUserIdByUsername(@Param("username") String username);

}
