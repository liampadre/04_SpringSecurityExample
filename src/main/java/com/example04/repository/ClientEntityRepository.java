package com.example04.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example04.model.ClientEntity;

public interface ClientEntityRepository extends JpaRepository<ClientEntity, Long> {

    Optional<ClientEntity> findByClientId(String clientId);

    Optional<ClientEntity> findByClientIdAndStatus(String clientId, ClientEntity.Status status);
}
