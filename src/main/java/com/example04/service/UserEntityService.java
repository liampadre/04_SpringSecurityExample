package com.example04.service;

import java.util.List;
import java.util.Optional;

import com.example04.model.UserEntity;
import com.example04.model.UserResponse;

public interface UserEntityService {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findById(Long id);

    UserResponse saveUser(UserEntity userEntity);

    List<UserResponse> getUsers();
}
