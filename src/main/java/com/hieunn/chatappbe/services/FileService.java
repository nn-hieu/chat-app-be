package com.hieunn.chatappbe.services;

import com.hieunn.chatappbe.dtos.responses.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    FileDTO uploadFile(MultipartFile file, Long maxSize, String destination);

    FileDTO uploadFile(MultipartFile file, String destination);

    List<FileDTO> uploadFiles(List<MultipartFile> files, String destination);

    void deleteFile(String publicId);
}
