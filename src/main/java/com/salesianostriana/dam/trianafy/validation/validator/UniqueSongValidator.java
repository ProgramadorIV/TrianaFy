package com.salesianostriana.dam.trianafy.validation.validator;

import com.salesianostriana.dam.trianafy.validation.annotation.UniqueSong;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueSongValidator implements ConstraintValidator<UniqueSong, Object> {

    private String name;
    private Long artistId;
    private String album;

    @Override
    public void initialize(UniqueSong constraintAnnotation) {

        name = constraintAnnotation.getName();
        artistId = constraintAnnotation.getArtistId();
        album = constraintAnnotation.getAlbum();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {


        return false;
    }
}
