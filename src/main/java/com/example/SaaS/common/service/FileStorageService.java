package com.example.SaaS.common.service;

import com.example.SaaS.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads/flags}")
    private String uploadDir ;
    public String saveFile(MultipartFile file){
        try {
            if (file == null || file.isEmpty()){
                return null; // skip silently
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")){
                throw new BusinessException("Only image files are allowed");
            }

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName != null && originalFileName.contains(".")
                    ? originalFileName.substring(originalFileName.lastIndexOf("."))
                    : ".png";

            String newFilename = UUID.randomUUID().toString() + fileExtension;

            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return newFilename;
        } catch (IOException e) {
            throw new BusinessException("Failed to store file: " + e.getMessage());
        }
    }


//    public String saveFile(MultipartFile file){
//        try {
//            if (file.isEmpty()){
//                throw new BusinessException("File is empty");
//            }
//
//            String contentType = file.getContentType();
//            if (contentType == null || !contentType.startsWith("image/")){
//                throw new BusinessException("only image files are allowed");
//            }
//
//            Path uploadPath = Paths.get(uploadDir);
//            if (!Files.exists(uploadPath)){
//                Files.createDirectories(uploadPath);
//            }
//
//            String originalFileName = file.getOriginalFilename();
//            String fileExtension = originalFileName != null
//                    ? originalFileName.substring(originalFileName.lastIndexOf("."))
//                    : ".png";
//            String newFilename = UUID.randomUUID().toString() + fileExtension;
//
//            Path filePath = uploadPath.resolve(newFilename);
//            Files.copy(file.getInputStream(), filePath,
//                    StandardCopyOption.REPLACE_EXISTING);
//
//            return newFilename;
//        }catch (IOException e){
//            throw  new BusinessException("Failed to store file: " + e.getMessage());
//        }
//    }

    public void deleteFile(String filename){
        if (filename == null || filename.isEmpty()){
            return;
        }

        try{
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Files.deleteIfExists(filePath);
        }catch (IOException e){
            throw  new BusinessException("Failed to delete file: " + e.getMessage());
        }
    }

    public byte[] loadFile(String filename){
        try{
            Path filePath = Paths.get(uploadDir).resolve(filename);
            if (!Files.exists(filePath)){
                throw new BusinessException("File not found: " + filename);
            }
            return Files.readAllBytes(filePath);
        }catch (IOException e){
            throw  new BusinessException("Failed to load file: " + e.getMessage());
        }
    }
}
