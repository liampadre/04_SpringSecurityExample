package com.example04.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example04.model.ProductEntity;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByName(String name);
}
