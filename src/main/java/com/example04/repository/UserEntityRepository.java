package com.example04.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example04.model.UserEntity;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
}
