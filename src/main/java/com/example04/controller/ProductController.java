package com.example04.controller;

import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example04.model.ProductRequest;
import com.example04.model.ProductResponse;
import com.example04.service.ProductEntityService;
import com.example04.util.ProductConverter;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductEntityService productEntityService;

    @PostMapping
    public ResponseEntity<ProductResponse> saveProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse body =
                productEntityService.saveProduct(ProductConverter.convertUserRequestToUserEntity(productRequest));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(body);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productEntityService.findAll());
    }

}
