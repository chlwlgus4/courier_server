package com.courier.handler;

import com.courier.handler.dto.ErrorDTO;
import com.courier.handler.exception.ErrorCode;
import com.courier.handler.exception.InvalidRefreshTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${cookie.secure:false}")
    private boolean cookieSecure;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> onValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(new ErrorDTO(ErrorCode.VALIDATION_FAILED, errors, request.getRequestURI()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDTO> onAuthFail(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDTO(ErrorCode.AUTH_FAILED, "아이디 또는 비밀번호가 틀렸습니다.", request.getRequestURI()));
    }


    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorDTO> onInvalidRefreshToken(
            InvalidRefreshTokenException ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(cookieSecure)    // 개발/운영 환경에 맞춰 분기
                .path("/")
                .maxAge(0)               // 즉시 만료
                .sameSite("Strict")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        ErrorDTO body = new ErrorDTO(
                ErrorCode.REFRESH_TOKEN_INVALID,
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }

}
