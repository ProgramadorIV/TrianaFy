package com.salesianostriana.dam.trianafy.error.model.errorattributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesianostriana.dam.trianafy.error.model.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@RequiredArgsConstructor
public class ApiCustomErrorAttributes extends DefaultErrorAttributes {

    private final ObjectMapper objectMapper;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest request, ErrorAttributeOptions attributeOptions){
        Map<String, Object> defaultErrorAttributes = super.getErrorAttributes(request, attributeOptions);
        ApiError apiError = ApiError.fromErrorAttributes(defaultErrorAttributes);

        return objectMapper.convertValue(apiError, Map.class);
    }
}
