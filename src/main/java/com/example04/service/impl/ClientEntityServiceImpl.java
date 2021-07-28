package com.example04.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example04.model.ClientEntity;
import com.example04.model.ClientResponse;
import com.example04.repository.ClientEntityRepository;
import com.example04.service.ClientEntityService;
import com.example04.util.ClientConverter;

@Service
public class ClientEntityServiceImpl implements ClientEntityService {

    private final ClientEntityRepository repository;

    private final PasswordEncoder passwordEncoder;

    public ClientEntityServiceImpl(ClientEntityRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ClientEntity> findByClientId(String clientId) {
        return repository.findByClientId(clientId);
    }

    @Override
    public Optional<ClientEntity> findByClientIdAndStatus(String clientId, ClientEntity.Status status) {
        return repository.findByClientIdAndStatus(clientId, status);
    }

    @Override
    public Optional<ClientEntity> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public ClientResponse saveClient(ClientEntity clientEntity) {
        //TODO Encode secret
        clientEntity.setClientSecret(passwordEncoder.encode(clientEntity.getClientSecret()));
        clientEntity.setStatus(ClientEntity.Status.ACTIVE);
        return ClientConverter.convertClientEntityToClientResponse(repository.save(clientEntity));
    }

    @Override
    public List<ClientResponse> getClients() {
        return repository.findAll().stream()
                .map(ClientConverter::convertClientEntityToClientResponse)
                .collect(Collectors.toList());
    }
}
