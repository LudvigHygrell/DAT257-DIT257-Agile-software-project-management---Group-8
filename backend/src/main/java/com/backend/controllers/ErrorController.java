package com.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController("/error")
class ErrorController {

    @GetMapping
    public ResponseEntity<String> accept() {
        return ResponseEntity.ok().body("Error fallback tripped.");
    }
}
