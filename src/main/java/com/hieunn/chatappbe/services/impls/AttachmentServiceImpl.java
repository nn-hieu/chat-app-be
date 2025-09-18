package com.hieunn.chatappbe.services.impls;

import com.hieunn.chatappbe.entities.Attachment;
import com.hieunn.chatappbe.repositories.AttachmentRepository;
import com.hieunn.chatappbe.services.AttachmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AttachmentServiceImpl implements AttachmentService {
    AttachmentRepository attachmentRepository;

    @Override
    public Resource findResource(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found with id: " + attachmentId));

        UrlResource resource;
        try {
            resource = new UrlResource(attachment.getUrl());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Malformed URL: " + attachment.getUrl());
        }

        return resource;
    }

    @Override
    public String findAttachmentName(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found with id: " + attachmentId));

        return attachment.getOriginalFilename();
    }
}