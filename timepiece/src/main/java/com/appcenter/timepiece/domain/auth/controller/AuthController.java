package com.appcenter.timepiece.domain.auth.controller;


import com.appcenter.timepiece.domain.auth.service.AuthService;
import com.appcenter.timepiece.global.common.dto.CommonResponse;
import com.appcenter.timepiece.global.config.SwaggerApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/v1/auth/reissue")
    @Operation(summary = "토큰 재발급", description = "")
    @SwaggerApiResponses
    public CommonResponse<?> reissueAccessToken(HttpServletRequest request) {
        return CommonResponse.success("토큰 재발급 성공", authService.reissueAccessToken(request));
    }

}
