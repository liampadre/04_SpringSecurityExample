package com.example04.util;

import com.example04.model.ProductEntity;
import com.example04.model.ProductRequest;
import com.example04.model.ProductResponse;

public class ProductConverter {

    public static ProductResponse convertProductEntityToProductResponse(ProductEntity productEntity) {
        return ProductResponse.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .build();
    }

    public static ProductEntity convertUserRequestToUserEntity(ProductRequest productRequest) {
        return ProductEntity.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
    }
}
