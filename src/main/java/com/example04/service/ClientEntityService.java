package com.example04.service;

import java.util.List;
import java.util.Optional;

import com.example04.model.ClientEntity;
import com.example04.model.ClientResponse;

public interface ClientEntityService {

    Optional<ClientEntity> findByClientId(String clientId);

    Optional<ClientEntity> findByClientIdAndStatus(String clientId, ClientEntity.Status status);

    Optional<ClientEntity> findById(Long id);

    ClientResponse saveClient(ClientEntity clientEntity);

    List<ClientResponse> getClients();
}
