package com.salesianostriana.dam.trianafy.dto;

//{“title”: “The song”, “artistId”: 1, “album” : “The album”, “year”: 2000}

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CreateSongDTO /*SongRequest*/ {

    private String title;
    private Long artistId;
    private String album;
    private String year;
}
