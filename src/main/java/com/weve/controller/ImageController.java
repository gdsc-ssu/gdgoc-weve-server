package com.weve.controller;

import com.weve.common.api.payload.BasicResponse;
import com.weve.domain.User;
import com.weve.service.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
@Tag(name = "OCR", description = "OCR 관련 API입니다.")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public BasicResponse<String> postOcrText(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        String username = userDetails.getUsername();
        return imageService.postOcrTextByFile(username, imageFile);
    }
}
