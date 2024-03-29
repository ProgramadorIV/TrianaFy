package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Song;
import org.springframework.stereotype.Component;

@Component
public class SongDTOConverter {

    public Song createSongDTOToSong (CreateSongDTO createSongDTO){
        return new Song(
                createSongDTO.getTitle(),
                createSongDTO.getAlbum(),
                createSongDTO.getYear()
        );

    }

    public GetSongDTO SongToGetSongDTO(Song song){
        return GetSongDTO.builder()
                .title(song.getTitle())
                .album(song.getAlbum())
                .year(song.getYear())
                .artist(song.getArtist()!=null?song.getArtist().getName():null)
                .build();
    }

    public GetSongWithArtistDTO songToGetSongWithArtistDTO(Song song){
        return  GetSongWithArtistDTO.builder()
                .id(song.getId())
                .title(song.getTitle())
                .artist(song.getArtist()!= null ?
                        GetArtistDTO.builder()
                        .id(song.getArtist().getId())
                        .artist(song.getArtist().getName())
                        .build()
                        :
                        null
                )
                .album(song.getAlbum())
                .year(song.getYear())
                .build();
    }
}
