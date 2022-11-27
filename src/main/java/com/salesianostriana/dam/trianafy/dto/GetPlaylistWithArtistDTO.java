package com.salesianostriana.dam.trianafy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
/*{
        “id”: 1,
        “name”: “The name”,
        “description”: “The desc”
        “songs”: [
        { “id”: 1, “title”: “The song”,
        “artist”: “Artist name”,
        “album” : “The album”, “year”: 2000},
        { “id”: 2, “title”: “Another song”,
        “artist”: “Another Artist name”,
        “album” : “Another album”, “year”: 2020},
        ...
        ]
        }*/

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GetPlaylistWithArtistDTO {

    private Long id;
    private String name;
    private String description;
    private List<GetSongDTO> songList = new ArrayList<>();
}
