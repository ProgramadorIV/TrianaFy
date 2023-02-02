package com.salesianostriana.dam.trianafy.validation.annotation;

import com.salesianostriana.dam.trianafy.validation.validator.UniqueArtistNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueArtistNameValidator.class)
@Documented
public @interface UniqueArtistName {

    String message() default "The artist provided is already registered";
    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};

}
