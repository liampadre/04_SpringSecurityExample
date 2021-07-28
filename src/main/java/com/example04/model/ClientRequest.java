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
public class ClientRequest {

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    private String name;

    private String description;

    @JsonProperty("grant_types")
    private List<String> grantTypes;

    @JsonProperty("redirect_urls")
    private List<String> redirectURLs;

    private Set<String> scopes;

    private Set<String> roles;
}
