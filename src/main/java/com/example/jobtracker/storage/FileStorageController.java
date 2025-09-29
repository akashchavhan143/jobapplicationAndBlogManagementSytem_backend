package com.example.jobtracker.storage;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileStorageController {

    @Autowired
    private  StorageService storageService;
    @Value("${com.onlineshopping.image.folder.path}")
    private String uploadDir;

//    @GetMapping("/{filename}")
//    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
//        try {
//            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//
//            if (resource.exists() || resource.isReadable()) {
//                return ResponseEntity.ok()
//                        .contentType(MediaType.IMAGE_JPEG) // Adjust as needed
//                        .body(resource);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build();
//        }
//    }

    // GET /v1/api/files/39e98b2b-4abb-49e4-a695-5e635ac50cb5.jpg
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable("filename") String filename) {
        try {
            // Basic path traversal protection
            Path base = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path filePath = base.resolve(filename).normalize();
            if (!filePath.startsWith(base)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            var resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // Inline for images; otherwise browser decides (most types display inline anyway)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.add("X-Content-Type-Options", "nosniff");

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file) {

        // Input validation
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new FileUploadResponse("File cannot be empty"));
        }

        try {
            String fileUrl = storageService.store(file);
            return ResponseEntity.ok(new FileUploadResponse(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    fileUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FileUploadResponse("Unexpected error: " + e.getMessage()));
        }
    }

    public static record FileUploadResponse(
            String filename,
            String contentType,
            long size,
            String fileUrl,
            String error
    ) {
        public FileUploadResponse(String filename, String contentType, long size, String fileUrl) {
            this(filename, contentType, size, fileUrl, null);
        }
        public FileUploadResponse(String error) {
            this(null, null, 0, null, error);
        }
    }
}
