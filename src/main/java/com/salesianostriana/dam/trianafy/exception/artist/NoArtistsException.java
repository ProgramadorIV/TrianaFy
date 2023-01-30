package com.salesianostriana.dam.trianafy.exception.artist;

import javax.persistence.EntityNotFoundException;

public class NoArtistsException extends EntityNotFoundException {

    public NoArtistsException(){super("No available artists.");}
}
