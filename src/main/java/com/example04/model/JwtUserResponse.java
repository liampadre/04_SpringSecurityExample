package com.example04.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
public class JwtUserResponse extends UserResponse {

    private String token;

    @Builder(builderMethodName = "jwtUserResponseBuilder")
    public JwtUserResponse(String username, String avatar, Set<String> roles, String token) {
        super(username, avatar, roles);
        this.token = token;
    }
}
