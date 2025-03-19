package com.weve.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.weve.common.api.payload.BasicResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

    private final ResourceLoader resourceLoader;

    @Value("${ocr.api.key-file}")
    private String ocrKeyFilePath;

    public BasicResponse<String> postOcrTextByFile(String username, MultipartFile imageFile) throws IOException {

        // 예외처리 추가
        try (ImageAnnotatorClient vision = createVisionClient()) {
            ByteString imgBytes = ByteString.readFrom(imageFile.getInputStream());
            Image image = Image.newBuilder().setContent(imgBytes).build();
            return analyzeImage(vision, image);
        }
    }

    // OCR 분석 로직
    private BasicResponse<String> analyzeImage(ImageAnnotatorClient vision, Image image) {
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION))
                .setImage(image)
                .build();

        BatchAnnotateImagesResponse response = vision.batchAnnotateImages(List.of(request));
        String extractedText = response.getResponses(0).getTextAnnotations(0).getDescription();

        return BasicResponse.onSuccess(extractedText);
    }

    // API 클라이언트
    private ImageAnnotatorClient createVisionClient() throws IOException {
        Resource resource = resourceLoader.getResource("file:" + ocrKeyFilePath);
        FileInputStream credentialsStream = new FileInputStream(resource.getFile());
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
        return ImageAnnotatorClient.create(ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(() -> credentials)
                .build());
    }
}
