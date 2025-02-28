package com.weve.controller;

import com.weve.common.api.payload.BasicResponse;
import com.weve.dto.response.UploadFileResponse;
import com.weve.service.GcsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api/storage")
public class GcsController {

    private final GcsService gcsService;

    @PostMapping("/upload")
    public BasicResponse<UploadFileResponse> uploadFile(@RequestPart(value = "file") MultipartFile multipartFile) throws IOException {


        String fileUrl = gcsService.processFile(gcsService.uploadFile(multipartFile));

        UploadFileResponse response = UploadFileResponse
                .builder()
                .fileUrl(fileUrl)
                .build();

        return BasicResponse.onSuccess(response);
    }
}
