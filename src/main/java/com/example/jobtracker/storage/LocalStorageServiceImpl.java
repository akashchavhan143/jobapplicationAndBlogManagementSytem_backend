package com.example.jobtracker.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Profile("dev")
public class LocalStorageServiceImpl implements StorageService {
    private static final Logger logger = LoggerFactory.getLogger(LocalStorageServiceImpl.class);
    @Value("${com.onlineshopping.image.folder.path}")
    private String basePath;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public List<String> loadAll() {
        return null;
    }

    @Override
    public String store(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String ext = "";
//            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            } else {
                ext = ".jpg"; // fallback if frontend sends blob without name
            }

            String fileName = UUID.randomUUID() + ext;
            File filePath = new File(basePath, fileName);
            logger.info("Storing file: originalName={}, generatedName={}, path={}",
                    file.getOriginalFilename(), fileName, filePath.getAbsolutePath());
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                FileCopyUtils.copy(file.getInputStream(), out);
            }
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/")
                    .path(fileName)
                    .toUriString();

            logger.info("File stored successfully. Accessible at: {}", fileUrl);

            return fileUrl;
        } catch (Exception e) {
            logger.error("Failed to store file locally: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to store file locally", e);
        }
    }

    @Override
    public Resource load(String fileName) {
        File filePath = new File(basePath, fileName);
        return filePath.exists() ? new FileSystemResource(filePath) : null;
    }

    @Override
    public void delete(String fileName) {
        File file = new File(basePath, fileName);
        boolean deleted = file.delete();
        if (deleted) {
            logger.info("Deleted file: {}", file.getAbsolutePath());
        } else {
            logger.warn("Failed to delete file (may not exist): {}", file.getAbsolutePath());
        }
    }
}