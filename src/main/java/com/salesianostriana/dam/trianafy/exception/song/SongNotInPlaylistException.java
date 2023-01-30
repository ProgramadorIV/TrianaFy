package com.salesianostriana.dam.trianafy.exception.song;

import javax.persistence.EntityNotFoundException;

public class SongNotInPlaylistException extends EntityNotFoundException {

    public SongNotInPlaylistException(Long id){super(String.format("The song with id: %d is not in this playlist.", id));}
}
