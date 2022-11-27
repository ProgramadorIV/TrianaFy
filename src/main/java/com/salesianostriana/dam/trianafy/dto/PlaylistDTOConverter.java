package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Playlist;
import org.springframework.stereotype.Component;

@Component
public class PlaylistDTOConverter {

    public Playlist createPlaylistDTOToPlaylist(CreatePlaylistDTO createPlaylistDTO){
        return new Playlist(
                createPlaylistDTO.getName(),
                createPlaylistDTO.getDescription()
        );
    }

    public GetPlaylistDTO playlistToGetPlaylistDTO(Playlist playlist){
        return GetPlaylistDTO.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .build();
    }

    public NumberOfSongsDTO playListToNumberOfSongsDTO(Playlist playlist){
        return NumberOfSongsDTO.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .numberOfSongs(playlist.getSongs()==null? 0 : playlist.getSongs().size())
                .build();
    }


}
