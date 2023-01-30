package com.salesianostriana.dam.trianafy.exception.song;

import javax.persistence.EntityNotFoundException;

public class NoSongsException extends EntityNotFoundException {

    public NoSongsException(){super("No songs available.");}
}
