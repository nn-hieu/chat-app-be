package com.hieunn.chatappbe.services.impls;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hieunn.chatappbe.dtos.responses.FileDTO;
import com.hieunn.chatappbe.services.FileService;
import com.hieunn.chatappbe.utils.FileUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {
    Cloudinary cloudinary;
    FileUtil fileUtil;

    @Override
    public FileDTO uploadFile(MultipartFile file, Long maxSize, String destination) {
        if (file.getSize() > maxSize) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "File size cannot be greater than " + maxSize / 1024 / 1024 + "MB"
            );
        }

        try {
            Map uploadResult = cloudinary
                    .uploader()
                    .upload(
                            file.getBytes(),
                            ObjectUtils.asMap(
                                    "folder", "chat-app" + destination,
                                    "resource_type", "auto"
                            )
                    );

            return FileDTO.builder()
                    .url((String) uploadResult.get("secure_url"))
                    .publicId((String) uploadResult.get("public_id"))
                    .type(fileUtil.getType(file))
                    .format(fileUtil.getFormat(file))
                    .name(file.getOriginalFilename())
                    .size(file.getSize())
                    .build();
        } catch (IOException e) {
            log.error("e: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not upload file");
        }
    }

    @Override
    public FileDTO uploadFile(MultipartFile file, String destination) {
        return uploadFile(file, 25 * 1024 * 1024L, destination);
    }

    @Override
    public List<FileDTO> uploadFiles(List<MultipartFile> files, String destination) {
        try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
            List<Future<FileDTO>> futures = new ArrayList<>();

            for (MultipartFile file : files) {
                futures.add(executor.submit(() -> uploadFile(file, destination)));
            }

            List<FileDTO> results = new ArrayList<>();
            for (Future<FileDTO> future : futures) {
                try {
                    results.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    log.error("e: ", e);
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Could not upload file"
                    );
                }
            }

            return results;
        }
    }

    @Override
    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            log.error("Could not delete file", e);
        }
    }
}
