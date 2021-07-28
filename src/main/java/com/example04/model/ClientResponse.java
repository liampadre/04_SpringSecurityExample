package com.example04.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponse {

    @JsonProperty("client_id")
    private String clientId;

    private String name;

    private String description;

    private Set<String> scopes;

    private String status;

    private Set<String> roles;

}
