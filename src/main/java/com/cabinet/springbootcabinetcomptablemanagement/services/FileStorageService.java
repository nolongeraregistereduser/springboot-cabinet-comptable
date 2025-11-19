package com.cabinet.springbootcabinetcomptablemanagement.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String storeFile(MultipartFile file);
    byte[] loadFile(String fileName);
    void deleteFile(String fileName);
    void deteleAllFiles();
}
