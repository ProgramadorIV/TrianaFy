package com.salesianostriana.dam.trianafy.error;

import com.salesianostriana.dam.trianafy.error.model.impl.ApiErrorImpl;
import com.salesianostriana.dam.trianafy.error.model.impl.ApiValidationSubError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler() //({clases que maneje}})
    public ResponseEntity<?> handleNotFoundException(EntityNotFoundException exception, WebRequest request){
        return buildApiError(exception.getMessage(), request, HttpStatus.NOT_FOUND);
    }

    private final ResponseEntity<Object> buildApiError(String message, WebRequest request, HttpStatus status){
        return ResponseEntity
                .status(status)
                .body(
                        ApiErrorImpl.builder()
                                .status(status)
                                .message(message)
                                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                                .build()
                );
    }

    private final ResponseEntity<Object> buildApiErrorWithSubErrors(String message, WebRequest request, HttpStatus status, List<ObjectError> subErrors){
        return ResponseEntity
                .status(status)
                .body(
                        ApiErrorImpl.builder()
                                .status(status)
                                .message(message)
                                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                                .subErrors(subErrors.stream()
                                        .map(ApiValidationSubError::fromObjectError)
                                        .collect(Collectors.toList())
                                )
                                .build()
                );
    }
}
