package creotech.crud.controller;

import creotech.crud.model.GeneralResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<GeneralResponse<Object>> handleResponseStatus(ResponseStatusException ex) {
        log.warn("RSE: status={}, reason={}", ex.getStatusCode(), ex.getReason());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(GeneralResponse.error(ex.getReason() != null ? ex.getReason() : "Request failed"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralResponse<Object>> handleBeanValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(err -> err.getDefaultMessage() != null ? err.getDefaultMessage() : "Invalid value")
                .toList();

        log.warn("Bean validation failed: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(GeneralResponse.error(errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GeneralResponse<Object>> handleConstraint(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        log.warn("Constraint violation: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(GeneralResponse.error(errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GeneralResponse<Object>> handleUnreadable(HttpMessageNotReadableException ex) {
        log.warn("Unreadable JSON body", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(GeneralResponse.error("Invalid JSON body"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GeneralResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = "Invalid value for parameter '%s'".formatted(ex.getName());
        log.warn("Type mismatch: {}", msg);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(GeneralResponse.error(msg));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<GeneralResponse<Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        String msg = "Missing required parameter '%s'".formatted(ex.getParameterName());
        log.warn("Missing param: {}", msg);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(GeneralResponse.error(msg));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GeneralResponse<Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation", ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(GeneralResponse.error("Data integrity violation"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralResponse<Object>> handleUnknown(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GeneralResponse.error("Internal server error"));
    }
}
