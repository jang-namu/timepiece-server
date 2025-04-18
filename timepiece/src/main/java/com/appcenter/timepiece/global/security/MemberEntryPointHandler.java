package com.appcenter.timepiece.global.security;

import com.appcenter.timepiece.global.common.dto.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class MemberEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("[ClientEntryPointException]인증 실패");

        CommonResponse commonResponse = new CommonResponse(0, "인증이 실패했습니다.", null);

        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        response.getWriter().write(objectMapper.writeValueAsString(commonResponse));

    }
}