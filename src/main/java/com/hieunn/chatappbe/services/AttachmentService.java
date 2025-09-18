package com.hieunn.chatappbe.services;

import org.springframework.core.io.Resource;

public interface AttachmentService {
    Resource findResource(Long attachmentId);

    String findAttachmentName(Long attachmentId);
}
