package com.sicredi.desafio.utils.handlers;

import com.sicredi.desafio.exceptions.VotacaoExceptions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "br.com.sicredi.desafio")
public class VotacaoExceptionHandler {

    @ExceptionHandler(VotacaoExceptions.TERegistroNaoEncontradoException.class)
    public ResponseEntity<Object> handleRegistroNaoEncontrado(VotacaoExceptions.TERegistroNaoEncontradoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VotacaoExceptions.TEStatusInvalidoException.class)
    public ResponseEntity<Object> handleStatusInvalido(VotacaoExceptions.TEStatusInvalidoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VotacaoExceptions.TEFiltroInvalidoException.class)
    public ResponseEntity<Object> handleFiltroInvalido(VotacaoExceptions.TEFiltroInvalidoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOutrasExceptions(Exception ex) {
        String msg = ex.getMessage();
        return buildResponse(msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
        String mensagens = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("; "));
        return new ResponseEntity<>(mensagens, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String rawMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

        String extractedMessage = rawMessage;
        if (rawMessage.contains("[")) {
            int start = rawMessage.indexOf("[") + 1;
            int end = rawMessage.indexOf("]", start);
            if (start > 0 && end > start) {
                extractedMessage = rawMessage.substring(start, end);
            }
        }
        return new ResponseEntity<>(extractedMessage, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }
}

