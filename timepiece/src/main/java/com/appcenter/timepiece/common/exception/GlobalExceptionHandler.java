package com.appcenter.timepiece.common.exception;

import com.appcenter.timepiece.common.dto.CommonResponse;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = NotFoundMemberException.class)
    public ResponseEntity<CommonResponse> handleNotFoundMemberException(NotFoundMemberException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse<>(0, ex.getMessage(), null));
    }

    @ExceptionHandler(value = FailedCreateTokenException.class)
    public ResponseEntity<CommonResponse> handleTokenCreateError(FailedCreateTokenException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponse(0, ex.getMessage(), null));
    }

    @ExceptionHandler(value = JwtEmptyException.class)
    public ResponseEntity<CommonResponse> handleJwtEmptyException(JwtEmptyException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonResponse<>(0, ex.getMessage(), null));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CommonResponse> handleExpiredJwtException() {
        log.error("[ExpiredJwtException] 토큰에러");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonResponse.error("토큰의 기한이 만료되었습니다.", null));
    }

    @ExceptionHandler(NotEnoughPrivilegeException.class)
    public CommonResponse<?> handleNotEnoughPrivilegeException() {
        return CommonResponse.error("프로젝트에 대한 권한이 없습니다.", null);
    }
}
