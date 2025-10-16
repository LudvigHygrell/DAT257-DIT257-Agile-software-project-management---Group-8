package com.backend.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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

import com.backend.exceptions.IllegalFilenameSequenceException;
import com.backend.filesystem.PrivateFilesystem;
import com.backend.filesystem.PublicFilesystem;

/**
 * Controller for uploading or downloading media to/from the server.
 * <p>
 * Benesphere utilizes a "filesystem" abstraction for media storage. There are two kinds of "filesystems",
 * public and private. A public filesystem is available for everyone that can access the site, but is typically
 * read-only, unless you have administrator access.
 * 
 * Private filesystems are filesystems that are entirely separate for each user, and every user has the authority
 * to download, upload or delete any file they wish to within their own system.
 */
@RestController
@RequestMapping("/api/files")
public class FileController {
    
    @Autowired
    private PrivateFilesystem privateFilesystem;

    @Autowired
    private PublicFilesystem publicFilesystem;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> loadFileFailedExceptionHandler(Exception ex) {
        return ResponseEntity.status(500).body("File operation failed");
    }

    @ExceptionHandler(IllegalFilenameSequenceException.class)
    public ResponseEntity<String> illegalFilename() {
        return ResponseEntity.badRequest().body("Filepath attack attempt detected - This incident will be reported.");
    }

    /**
     * Performs validation on the given path and throws if it contains unexpected directory traversal
     * or suspicious elements.
     */
    private void ensureLegalFilepath(String filename) throws IllegalFilenameSequenceException {
        final String[] ILLEGAL_SEQUENCES = {
            "..",          // Attempt to traverse in reverse
            "~",           // Attempt to access HOME
            ":",           // Separator in some environment variables
            "//",          // Potential attempt to switch protocols
            "/etc/passwd", // Obvious
            "boot.ini",    // Windows boot script
            "\\\\\\\\",    // Windows live desktop
            "\0",          // String escape
            "$",           // Environment variable evaluation or arbitrary code
            ";",           // Attempt to end statement prematurely
            "%",           // Environment variable evaluation on windows
        };

        for (int codepoint : filename.codePoints().toArray()) {
            if (Character.isISOControl(codepoint)) // Control character
                throw new IllegalFilenameSequenceException();
            if (codepoint == '\r' || codepoint == '\n') // Dobious whitespace (attempt to hide a file duplication)
                throw new IllegalFilenameSequenceException();
        }

        for (String sequence : ILLEGAL_SEQUENCES) {
            if (filename.contains(sequence))
                throw new IllegalFilenameSequenceException();
        }
    }

    @GetMapping("/public/{filename:.+}")
    public ResponseEntity<Resource> publicDownload(@PathVariable String filename) throws Exception {
        ensureLegalFilepath(filename);

        Resource resource = publicFilesystem.load(filename);

        if (!resource.exists() || !resource.isReadable())
            return ResponseEntity.notFound().build();

        String contentType = Files.probeContentType(Paths.get(resource.getFilename()));
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""  + resource.getFilename() + "\"")
            .body(resource);
    }

    @GetMapping("/private/{filename:.+}")
    public ResponseEntity<Resource> privateDownload(@PathVariable String filename) throws IOException, IllegalFilenameSequenceException {
        ensureLegalFilepath(filename);

        Resource resource = privateFilesystem.load(filename);

        if (!resource.exists() || !resource.isReadable())
            return ResponseEntity.notFound().build();

        String contentType = Files.probeContentType(Paths.get(resource.getFilename()));
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""  + resource.getFilename() + "\"")
            .body(resource);
    }

    @PostMapping("/private")
    public ResponseEntity<String> privateUpload(@RequestParam("file") MultipartFile file) throws IOException, IllegalFilenameSequenceException {
        ensureLegalFilepath(file.getOriginalFilename());

        Path path = privateFilesystem.save(file);
        return ResponseEntity.created(path.toUri()).body("success");
    }

    @DeleteMapping("/private/{filename:.+}")
    public ResponseEntity<String> privateDelete(@PathVariable String filename) throws IOException, IllegalFilenameSequenceException {
        ensureLegalFilepath(filename);

        privateFilesystem.delete(filename);
        return ResponseEntity.ok().body("success");
    }
}
