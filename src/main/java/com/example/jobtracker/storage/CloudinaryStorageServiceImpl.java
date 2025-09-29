package com.example.jobtracker.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Profile("prod")
@ConditionalOnBean(Cloudinary.class)
public class CloudinaryStorageServiceImpl implements StorageService {

    private final Cloudinary cloudinary;


    public CloudinaryStorageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public List<String> loadAll() {
        return null;
    }

    @Override
    public String store(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
    }
//    public String store(MultipartFile file) {
//        // Determine folder based on file type
//        String folder = getFolderForFileType(file.getContentType());
//
//        Map<?, ?> uploadResult = null;
//        try {
//            uploadResult = cloudinary.uploader().upload(file.getBytes(),
//                    ObjectUtils.asMap(
//                            "folder", folder,
//                            "resource_type", "auto", // Automatically detect image/raw file
//                            "use_filename", true,
//                            "unique_filename", false
//                    ));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return uploadResult.get("secure_url").toString();
//    }
//
//    private String getFolderForFileType(String contentType) {
//        if (contentType == null) {
//            return "misc";
//        }
//
//        return switch (contentType.split("/")[0]) {
//            case "image" -> "images";
//            case "application" -> {
//                if (contentType.equals("application/pdf")) {
//                    yield "documents/pdf";
//                }
//                yield "documents";
//            }
//            case "text" -> "documents/text";
//            case "video" -> "videos";
//            case "audio" -> "audio";
//            default -> "misc";
//        };
//    }


    @Override
    public Resource load(String fileName) {
        throw new UnsupportedOperationException("Direct file loading not supported for Cloudinary");
    }

    @Override
    public void delete(String fileName) {
        try {
            String publicId = extractPublicIdFromUrl(fileName);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from Cloudinary", e);
        }
    }

    private String extractPublicIdFromUrl(String url) {
        // Extract public ID from Cloudinary URL
        int startIndex = url.lastIndexOf("/") + 1;
        int endIndex = url.lastIndexOf(".");
        return url.substring(startIndex, endIndex);
    }
}