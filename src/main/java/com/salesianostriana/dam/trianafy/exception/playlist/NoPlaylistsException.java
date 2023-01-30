package com.salesianostriana.dam.trianafy.exception.playlist;

import javax.persistence.EntityNotFoundException;

public class NoPlaylistsException extends EntityNotFoundException {

    public NoPlaylistsException(){super("No playlists available.");}
}
