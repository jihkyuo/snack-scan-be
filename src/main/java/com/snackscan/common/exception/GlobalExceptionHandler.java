package com.snackscan.common.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.snackscan.common.dto.ErrorResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 비즈니스 예외 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(
            BusinessException ex, WebRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        String uri = request.getDescription(false).replace("uri=", "");
        
        log.warn("BusinessException 발생 - URI: {}, ErrorCode: {}, Message: {}", 
            uri, errorCode.getCode(), ex.getMessage());
        
        ErrorResponseDto errorResponse = ErrorResponseDto.of(
            errorCode.getHttpStatus().value(),
            errorCode.getHttpStatus().getReasonPhrase(),
            ex.getMessage(),
            uri,
            errorCode.getCode()
        );
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    // 유효성 검증 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse("유효성 검증 실패");
        
        String uri = request.getDescription(false).replace("uri=", "");
        log.warn("유효성 검증 실패 - URI: {}, Message: {}", uri, message);

        ErrorResponseDto errorResponse = ErrorResponseDto.of(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            message,
            uri,
            CommonErrorCode.INVALID_INPUT.getCode()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        ErrorResponseDto errorResponse = ErrorResponseDto.of(
            HttpStatus.BAD_REQUEST.value(),
            "Constraint Violation",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""),
            CommonErrorCode.INVALID_INPUT.getCode()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 일반적인 예외 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        ErrorResponseDto errorResponse = ErrorResponseDto.of(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Argument",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""),
            CommonErrorCode.INVALID_INPUT.getCode()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalStateException(
            IllegalStateException ex, WebRequest request) {
        ErrorResponseDto errorResponse = ErrorResponseDto.of(
            HttpStatus.CONFLICT.value(),
            "Illegal State",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""),
            CommonErrorCode.INVALID_INPUT.getCode()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // 예상치 못한 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(
            Exception ex, WebRequest request) {
        String uri = request.getDescription(false).replace("uri=", "");
        log.error("예상치 못한 예외 발생 - URI: {}", uri, ex);
        
        ErrorResponseDto errorResponse = ErrorResponseDto.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "서버 내부 오류가 발생했습니다.",
            uri,
            CommonErrorCode.INTERNAL_SERVER_ERROR.getCode()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
