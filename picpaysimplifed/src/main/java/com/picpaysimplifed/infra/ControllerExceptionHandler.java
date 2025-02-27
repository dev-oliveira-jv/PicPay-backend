package com.picpaysimplifed.infra;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.picpaysimplifed.dtos.ExceptionDTO;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(exception = DataIntegrityViolationException.class)
    public ResponseEntity treatDuplicateEntry(DataIntegrityViolationException exception){
        ExceptionDTO exceptionDTO = new ExceptionDTO("Usuário já cadastrado", "400");
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(exception = EntityNotFoundException.class)
    public ResponseEntity treat404(EntityNotFoundException exception){
        return ResponseEntity.notFound().build();
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(Exception.class)
    public ResponseEntity treatGeneralException(Exception exception){
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), "500");
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }

}
