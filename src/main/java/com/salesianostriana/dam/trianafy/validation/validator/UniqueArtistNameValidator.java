package com.salesianostriana.dam.trianafy.validation.validator;

import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.validation.annotation.UniqueArtistName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class UniqueArtistNameValidator implements ConstraintValidator<UniqueArtistName, String> {


    @Autowired
    private ArtistService artistService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        System.out.println(value);
        return !artistService.existByName(value);
    }
}
