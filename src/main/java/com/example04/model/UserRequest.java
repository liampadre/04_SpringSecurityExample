package com.example04.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank
    private String username;

    private String avatar;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordRepeated;

}
