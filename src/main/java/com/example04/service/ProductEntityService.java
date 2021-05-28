package com.example04.service;

import java.util.List;

import com.example04.model.ProductEntity;
import com.example04.model.ProductResponse;

public interface ProductEntityService {

    List<ProductResponse> findAll();

    List<ProductResponse> findByName(String name);

    ProductResponse saveProduct(ProductEntity productEntity);
}
