package com.salesianostriana.dam.trianafy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GetSongWithArtistDTO {

    private Long id;
    private String title;
    private GetArtistDTO artist;
    private String album;
    private String year;
}
