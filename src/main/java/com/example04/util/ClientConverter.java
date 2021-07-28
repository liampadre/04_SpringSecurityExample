package com.example04.util;

import java.util.stream.Collectors;

import com.example04.model.ClientEntity;
import com.example04.model.ClientRequest;
import com.example04.model.ClientResponse;
import com.example04.model.UserEntity;
import com.example04.model.UserRequest;
import com.example04.model.UserResponse;

public class ClientConverter {

    public static ClientResponse convertClientEntityToClientResponse(ClientEntity clientEntity) {
        return ClientResponse.builder()
                .clientId(clientEntity.getClientId())
                .name(clientEntity.getName())
                .description(clientEntity.getDescription())
                .status(clientEntity.getStatus().name())
                .roles(clientEntity.getRoles())
                .scopes(clientEntity.getScopes())
                .build();
    }

    public static ClientEntity convertClientRequestToClientEntity(ClientRequest clientRequest) {
        return ClientEntity.builder()
                .clientId(clientRequest.getClientId())
                .clientSecret(clientRequest.getClientSecret())
                .name(clientRequest.getName())
                .description(clientRequest.getDescription())
                .grantTypes(clientRequest.getGrantTypes()
                        .stream().map(ClientEntity.GrantTypes::valueOf)
                        .collect(Collectors.toSet()))
                .redirectURLs(clientRequest.getRedirectURLs())
                .roles(clientRequest.getRoles())
                .scopes(clientRequest.getScopes())
                .build();
    }
}
