package com.backend.filesystem;

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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.ApplicationProperties;
import com.backend.interfaces.Filesystem;
import com.backend.jwt.user.UserUtil;

/**
 * Collection of resources that is unique for a single user.
 */
@Service
public class PrivateFilesystem implements Filesystem {
    
    @Autowired
    private ApplicationProperties properties;
    
    private final ResourceLoader loader;

    public PrivateFilesystem(ResourceLoader loader) {
        this.loader = loader;
    }

    @Override
    public Path save(MultipartFile file) throws IOException {

        final String root = getWriteRoot();

        Path path = Paths.get(root, 
            URLEncoder.encode(UserUtil.getUsername(), StandardCharsets.UTF_8), 
            file.getOriginalFilename());
        Files.createDirectories(path.getParent());
        try (OutputStream fout = Files.newOutputStream(path, 
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            file.getInputStream().transferTo(fout);
        }
        return path;
    }

    @Override
    public Resource load(String filename) {
        return loader.getResource(String.format("classpath:%s/%s/%s", 
            getReadRoot(), URLEncoder.encode(UserUtil.getUsername(), StandardCharsets.UTF_8), filename));
    }

    @Override
    public void delete(String filename) throws IOException {
        Path path = Paths.get(getWriteRoot(), 
            URLEncoder.encode(UserUtil.getUsername(), StandardCharsets.UTF_8), 
            filename); 
        Files.delete(path);
    }

    /**
     * Get the root directory used when reading files.
     */
    public String getReadRoot() {
        return properties.getFile().getPrivateDirectories().getReadDirectory();
    }

    /**
     * Get the root directory used when writing files.
     * @throws IOException Thrown if the path could not be resolved.
     */
    public String getWriteRoot() throws IOException {
        return loader.getResource(String.format("classpath:%s", 
            properties.getFileProperties().getPrivateDirectories().getWriteDirectory()))
            .getURI().getPath();
    }
}
