package com.example04.controller;

import lombok.RequiredArgsConstructor;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    @GetMapping()
    public ResponseEntity<List<String>> getEmployees() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Arrays.asList("Mariana", "Zoila", "Victor"));
    }

}
