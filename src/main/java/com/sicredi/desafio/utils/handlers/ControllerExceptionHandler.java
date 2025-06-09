package com.sicredi.desafio.utils.handlers;

import com.sicredi.desafio.utils.CustomErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;


@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StringBuilder errorMessage = new StringBuilder("Parâmetros inválidos: ");

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String defaultMessage = error.getDefaultMessage();
            errorMessage.append(String.format("Campo '%s': %s; ", fieldName, defaultMessage));
        });

        CustomErrorResponse err = new CustomErrorResponse(Instant.now(), status.value(), errorMessage.toString(), request.getDescription(false));
        log.error("Erro de validação ocorreu: {}", err.error());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CustomErrorResponse> handleMissingParam(MissingServletRequestParameterException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StringBuilder errorMessage = new StringBuilder("Parâmetro obrigatório ausente: " + ex.getParameterName());

        CustomErrorResponse err = new CustomErrorResponse(Instant.now(), status.value(), errorMessage.toString(), request.getDescription(false));
        log.error("Erro de validação ocorreu: {}", err.error());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<CustomErrorResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String errorMessage = "Nenhum resultado encontrado para a operação realizada.";

        CustomErrorResponse err = new CustomErrorResponse(Instant.now(), status.value(), errorMessage, request.getRequestURI());
        log.error("Erro de acesso a dados: {}", err.error());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<CustomErrorResponse> handleDateTimeParseException(DateTimeParseException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorMessage = "Formato de data/hora inválido: " + ex.getParsedString();

        CustomErrorResponse err = new CustomErrorResponse(Instant.now(), status.value(), errorMessage, request.getRequestURI());
        log.error("Erro de formatação de data/hora ocorreu: {}", err.error());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> String.format("Campo '%s': %s",
                        violation.getPropertyPath(),
                        violation.getMessage()))
                .collect(Collectors.joining("; "));

        CustomErrorResponse err = new CustomErrorResponse(
                Instant.now(),
                status.value(),
                errorMessage,
                request.getDescription(false)
        );

        log.error("Erro de validação ocorreu: {}", err.error());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorMessage = "Tipo de argumento inválido para o parâmetro '" + ex.getName() + "'. Esperado: " + ex.getRequiredType().getSimpleName() + ", mas recebido: " + ex.getValue().getClass().getSimpleName();

        CustomErrorResponse err = new CustomErrorResponse(Instant.now(), status.value(), errorMessage, request.getRequestURI());
        log.debug("Erro de tipo de argumento ocorreu: {}", err.error());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        CustomErrorResponse err = new CustomErrorResponse(Instant.now(), status.value(), ex.getMessage(),request.getRequestURI());
        log.error("Erro inesperado ocorreu: {}", ex);
        return ResponseEntity.status(status).body(err);
    }

}
