package com.example04.controller;

import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example04.model.JwtUserResponse;
import com.example04.model.UserEntity;
import com.example04.model.UserRequest;
import com.example04.security.jwt.JwtProvider;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    @PostMapping("/auth/login")
    public JwtUserResponse login(@Valid @RequestBody UserRequest userRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserEntity user = (UserEntity) authentication.getPrincipal();
        String jwtToken = jwtProvider.generateToken(authentication);

        return convertUserEntityAndTokenToJwtUserResponse(user, jwtToken);
    }

    private JwtUserResponse convertUserEntityAndTokenToJwtUserResponse(UserEntity user, String jwtToken) {
        return JwtUserResponse
                .jwtUserResponseBuilder()
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
                .token(jwtToken)
                .build();
    }
}
