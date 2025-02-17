package com.tsg.authentication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/custom-health-check")
    public ResponseEntity<String> myCustomCheck() {
        return new ResponseEntity<>("Service OK", HttpStatus.OK);
    }
}