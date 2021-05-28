package com.example04.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example04.error.exception.NewUserWithDifferentPasswordsException;
import com.example04.model.UserEntity;
import com.example04.model.UserRequest;
import com.example04.model.UserResponse;
import com.example04.service.UserEntityService;
import com.example04.util.UserConverter;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserEntityService userEntityService;

    @PostMapping()
    public ResponseEntity<UserResponse> saveUser(@RequestBody UserRequest userRequest) {
        try {
            if (!userRequest.getPassword().equals(userRequest.getPasswordRepeated())) {
                throw new NewUserWithDifferentPasswordsException();
            }
            UserEntity newUser = UserConverter.convertUserRequestToUserEntity(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(userEntityService.saveUser(newUser));
        } catch(DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userEntityService.getUsers());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserEntity userEntity) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(UserConverter.convertUserEntityToUserResponse(userEntity));
    }
}
