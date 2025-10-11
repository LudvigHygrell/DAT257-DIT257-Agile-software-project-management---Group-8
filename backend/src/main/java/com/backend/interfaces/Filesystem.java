package com.backend.interfaces;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Abstraction of a filesystem that can be used to save or retrieve resources.
 */
public interface Filesystem {
    
    /**
     * Saves a new resource into the filesystem.
     * @param file Multipart file to save.
     * @return The final path of the resource.
     * @throws IOException Thrown if saving the resource failed.
     */
    public Path save(MultipartFile file) throws IOException;

    /**
     * Loads a resource that was stored in the filesystem.
     * @param filepath Path to the stored resource.
     * @return A resource object that represents the resource.
     */
    public Resource load(String filepath);

    /**
     * Deletes a resource from the filesystem.
     * @param filepath Path to the resource to delete.
     * @throws IOException Thrown if deleting the resource failed.
     */
    public void delete(String filepath) throws IOException;
}