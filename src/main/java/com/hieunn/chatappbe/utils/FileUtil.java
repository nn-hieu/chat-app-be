package com.hieunn.chatappbe.utils;

import com.hieunn.chatappbe.entities.enums.FileType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileUtil {
    Tika tika;

    public FileUtil() {
        tika = new Tika();
    }

    public String getMimeType(MultipartFile file) {
        try {
            return tika.detect(file.getInputStream());
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Cannot detect mime type of file: " + file.getOriginalFilename()
            );
        }
    }

    public FileType getType(MultipartFile file) {
        String[] parts = getMimeType(file).split("/", 2);
        try {
            return FileType.valueOf(parts[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return FileType.OTHER;
        }
    }

    public String getFormat(MultipartFile file) {
        String[] parts = getMimeType(file).split("/", 2);

        return parts[1];
    }

    public List<MultipartFile> getValidFiles(List<MultipartFile> files) {
        Map<String, List<String>> extMimeMap = Map.of(
                "png", List.of("image/png"),
                "jpg", List.of("image/jpeg"),
                "jpeg", List.of("image/jpeg"),
                "pdf", List.of("application/pdf"),
                "docx", List.of("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/x-tika-ooxml"),
                "xlsx", List.of("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/x-tika-ooxml"),
                "txt", List.of("text/plain")
        );

        List<MultipartFile> validFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            if (filename == null || !filename.contains(".")) {
                continue;
            }

            String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

            List<String> allowedMimes = extMimeMap.get(extension);
            String mime = getMimeType(file);
            if (allowedMimes != null && allowedMimes.contains(mime)) {
                validFiles.add(file);
            }
        }

        return validFiles;
    }
}
