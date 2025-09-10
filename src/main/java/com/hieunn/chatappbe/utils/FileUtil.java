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
}
