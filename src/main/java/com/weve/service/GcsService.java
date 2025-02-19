package com.weve.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GcsService {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private final Storage storage;

    public String uploadAudio(byte[] file) {

        String uuid = UUID.randomUUID().toString(); // Google Cloud Storage에 저장될 파일 이름

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, uuid)
                        .setContentType("audio/mpeg")
                        .build(),
                file
        );

        return uuid;
    }

//    public String uploadImage(MultipartFile image) throws IOException {
//
//        String uuid = UUID.randomUUID().toString(); // Google Cloud Storage에 저장될 파일 이름
//        String ext = image.getContentType();
//
//        BlobInfo blobInfo = storage.create(
//                BlobInfo.newBuilder(bucketName, uuid)
//                        .setContentType(ext)
//                        .build(),
//                image.getInputStream()
//        );
//
//        return uuid;
//    }

    public String processFile(String file) {

        if (StringUtils.isBlank(file)) {
            return null;
        }
        return "https://storage.googleapis.com/" + bucketName + "/" + file;
    }
}