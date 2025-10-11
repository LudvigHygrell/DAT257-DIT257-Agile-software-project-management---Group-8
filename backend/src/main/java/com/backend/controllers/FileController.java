package com.backend.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.ApplicationProperties;
import com.backend.jwt.user.UserUtil;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private ApplicationProperties properties;
    
    private final ResourceLoader loader;

    public FileController(ResourceLoader loader) {
        this.loader = loader;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> loadFileFailedExceptionHandler(Exception ex) {
        return ResponseEntity.status(500).body("File operation failed");
    }

    @GetMapping("/public/{filename:.+}")
    public ResponseEntity<Resource> publicDownload(@PathVariable String filename) throws Exception {
        
        final String root = properties.getFileProperties().getPublicDirectories().getReadDirectory();

        Resource resource = loader.getResource(String.format("classpath:%s/%s", root, filename));

        if (!resource.exists() || !resource.isReadable())
            return ResponseEntity.notFound().build();

        String contentType = Files.probeContentType(Paths.get(resource.getFilename()));
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""  + resource.getFilename() + "\"")
            .body(resource);
    }

    @GetMapping("/private/{filename:.+}")
    public ResponseEntity<Resource> privateDownload(@PathVariable String filename) throws IOException {
        
        final String root = properties.getFileProperties().getPrivateDirectories().getReadDirectory();

        Resource resource = loader.getResource(String.format("classpath:%s/%s/%s", 
            root, URLEncoder.encode(UserUtil.getUsername(), StandardCharsets.UTF_8), filename));

        if (!resource.exists() || !resource.isReadable())
            return ResponseEntity.notFound().build();

        String contentType = Files.probeContentType(Paths.get(resource.getFilename()));
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""  + resource.getFilename() + "\"")
            .body(resource);
    }

    @PostMapping("/private")
    public ResponseEntity<String> privateUpload(@RequestParam("file") MultipartFile file) throws IOException {
        
        final String root = loader.getResource(String.format("classpath:%s", 
            properties.getFileProperties().getPrivateDirectories().getWriteDirectory()))
            .getURI().getPath();

        Path path = Paths.get(root, 
            URLEncoder.encode(UserUtil.getUsername(), StandardCharsets.UTF_8), 
            file.getOriginalFilename());
        Files.createDirectories(path.getParent());
        try (OutputStream fout = Files.newOutputStream(path, 
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            file.getInputStream().transferTo(fout);
        }
        return ResponseEntity.created(path.toUri()).body("success");
    }

    @DeleteMapping("/private/{filename:.+}")
    public ResponseEntity<String> privateDelete(@PathVariable String filename) throws IOException {
        
        final String root = loader.getResource(String.format("classpath:%s", 
            properties.getFileProperties().getPrivateDirectories().getWriteDirectory()))
            .getURI().getPath();

        Path path = Paths.get(root, URLEncoder.encode(UserUtil.getUsername(), StandardCharsets.UTF_8), filename); 
        Files.delete(path);
        
        return ResponseEntity.ok().body("success");
    }
}
