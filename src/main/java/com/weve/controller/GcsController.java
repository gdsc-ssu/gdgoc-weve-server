package com.weve.controller;

import com.weve.common.api.payload.BasicResponse;
import com.weve.dto.response.UploadFileResponse;
import com.weve.service.GcsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
<<<<<<< HEAD
=======
import org.springframework.http.MediaType;
>>>>>>> 4904f89120c9db955ca9d1633e441ec857129ab3
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
@Tag(name = "Storage", description = "Goolge Cloud Storage 관련 API입니다.")
public class GcsController {

    private final GcsService gcsService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "파일 업로드", description = "파일을 업로드하여 URL을 생성합니다.")
    public BasicResponse<UploadFileResponse> uploadFile(@RequestPart(value = "file") MultipartFile multipartFile) throws IOException {


        String fileUrl = gcsService.processFile(gcsService.uploadFile(multipartFile));

        UploadFileResponse response = UploadFileResponse
                .builder()
                .fileUrl(fileUrl)
                .build();

        return BasicResponse.onSuccess(response);
    }
}
