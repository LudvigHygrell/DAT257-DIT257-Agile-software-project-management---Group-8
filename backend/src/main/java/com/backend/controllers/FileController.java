package com.backend.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.ApplicationProperties;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private ApplicationProperties properties;
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity loadFileFailedExceptionHandler() {
        return ResponseEntity.status(404).body("Failed to load resource.");
    }

    @GetMapping("/public/**")
    public ResponseEntity<Resource> publicDownload(HttpServletRequest request) throws Exception {

        final String path = ControllerHelper.sanitizePathSuffix(
        ControllerHelper.getRemainingPath(request));

        File file = new File(new ClassPathResource(properties.getFileProperties()
            .getPublicDirectories().getReadDirectory()).getPath(), path);
        
        return ResponseEntity.ok().body(new ByteArrayResource(
            Files.readAllBytes(Paths.get(file.getPath()))));
    }
}
