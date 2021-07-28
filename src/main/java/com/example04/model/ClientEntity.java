package com.example04.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data @NoArgsConstructor @AllArgsConstructor
@Builder
public class ClientEntity {

    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    private String name;

    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @JsonProperty("redirect_urls")
    private List<String> redirectURLs;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> scopes;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @JsonProperty("grant_types")
    private Set<GrantTypes> grantTypes;

    private Status status;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    public enum GrantTypes {
        AUTHORIZATION_CODE, IMPLICIT, PASSWORD, REFRESH_TOKEN, CLIENT_CREDENTIALS;
    }

    public enum Status {
        ACTIVE, REVOKED
    }
}
