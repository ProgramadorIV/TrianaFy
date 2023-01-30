package com.salesianostriana.dam.trianafy.exception.song;

import javax.persistence.EntityNotFoundException;

public class SongNotFoundException extends EntityNotFoundException {

    public SongNotFoundException(Long id){super(String.format("The song with id: %d could not be found", id));}
}
