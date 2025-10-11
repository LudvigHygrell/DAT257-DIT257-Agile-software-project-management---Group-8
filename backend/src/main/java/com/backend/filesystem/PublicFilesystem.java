package com.backend.filesystem;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.ApplicationProperties;
import com.backend.database.adapters.AuthorityInfo;
import com.backend.interfaces.Filesystem;

/**
 * Filesystem for files accessible by all clients.
 */
@Service
public class PublicFilesystem implements Filesystem {

    @Autowired
    private ApplicationProperties properties;
    
    @Autowired
    private AuthorityInfo authInfo;

    private final ResourceLoader loader;

    public PublicFilesystem(ResourceLoader loader) {
        this.loader = loader;
    }

    @Override
    public Path save(MultipartFile file) throws IOException {

        if (authInfo.getAuthority() < 1)
            throw new IOException("User is not authorized.");

        Path path = Paths.get(getWriteRoot(), file.getOriginalFilename());
        
        Files.createDirectories(path);
        try (OutputStream os = Files.newOutputStream(path)) {
            file.getInputStream().transferTo(os);
        }
        return path;
    }

    @Override
    public Resource load(String filepath) {
        Path path = Paths.get(getReadRoot(), filepath);
        return loader.getResource("classpath:" + path.toString());
    }

    @Override
    public void delete(String file) throws IOException {
        
        if (authInfo.getAuthority() < 2)
            throw new IOException("User is not authorized.");
        
        Files.delete(Paths.get(getWriteRoot(), file));
    }

    /**
     * Get the root directory used when reading files.
     */
    public String getReadRoot() {
        return properties.getFileProperties().getPublicDirectories().getReadDirectory();
    }

    /**
     * Get the root directory used when writing files.
     * @throws IOException Thrown if the path could not be resolved.
     */
    public String getWriteRoot() throws IOException {

        return loader.getResource(String.format("classpath:%s", 
            properties.getFileProperties().getPublicDirectories().getWriteDirectory()))
            .getURI().getPath();
    }
}
