package com.salesianostriana.dam.trianafy.exception.playlist;

import javax.persistence.EntityNotFoundException;

public class PlaylistNotFoundException extends EntityNotFoundException {

    public PlaylistNotFoundException(Long id){super(String.format("Playlist with id: %d could not be found.", id));}
    public PlaylistNotFoundException(){super("Playlist could not be found");}
}
