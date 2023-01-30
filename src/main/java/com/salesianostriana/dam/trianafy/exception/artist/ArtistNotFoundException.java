package com.salesianostriana.dam.trianafy.exception.artist;

import javax.persistence.EntityNotFoundException;

public class ArtistNotFoundException extends EntityNotFoundException {

    public ArtistNotFoundException(Long id) {super(String.format("Artist with id: %d could not be found", id));}
    public ArtistNotFoundException(){super("The artist could not be found.");}
}
