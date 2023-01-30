package com.salesianostriana.dam.trianafy.error.model;

import com.salesianostriana.dam.trianafy.error.model.impl.ApiErrorImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ApiError {

    HttpStatus getStatus();

    int getStatusCode();

    String getMessage();

    String getPath();

    LocalDateTime getDate();

    List<ApiSubError> getSubErrors();


    static ApiError fromErrorAttributes(Map<String, Object> defaultErrorAttributes){

        int statusCode = -1;
        HttpStatus status = null;

        if(defaultErrorAttributes.containsKey("status")){
            if(defaultErrorAttributes.get("status") instanceof Integer){
                statusCode = ((Integer) ((Integer) defaultErrorAttributes.get("status")).intValue());
                status = HttpStatus.valueOf(statusCode);
            } else if(defaultErrorAttributes.get("status") instanceof String){
                status = HttpStatus.valueOf((String) defaultErrorAttributes.get("status"));
                statusCode = status.value();
            }
        }

        ApiErrorImpl result = ApiErrorImpl.builder()
                .status(status)
                .statusCode(statusCode)
                .message((String) defaultErrorAttributes.getOrDefault("message", "No message available"))
                .path((String) defaultErrorAttributes.getOrDefault("path", "No path available"))
                .build();

        if(defaultErrorAttributes.containsKey("errors")){

            List<ObjectError> errors = (List<ObjectError>) defaultErrorAttributes.get("error");

            List<ApiSubError> subErrors = errors.stream()
                    .map(ApiValidationSubError::fromObjectError)
                    .toList();

            result.setSubErrors(subErrors);
        }

        return result;
    }
}
