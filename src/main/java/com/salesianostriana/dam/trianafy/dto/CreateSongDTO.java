package com.salesianostriana.dam.trianafy.dto;

//{“title”: “The song”, “artistId”: 1, “album” : “The album”, “year”: 2000}

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data @AllArgsConstructor @NoArgsConstructor
public class CreateSongDTO /*SongRequest*/ {

    @NotEmpty(message = "{createSongDTO.title.notempty}")
    private String title;

    //@Min( value = 1, message = "{createSongDTO.artistId.min}")
    @Positive(message = "{createSongDTO.artistId.positive}")
    @Digits(fraction = 0, integer = 10,message = "{createSongDTO.artistId.digits}")
    private Long artistId;
    private String album;

    @NotEmpty(message = "{createSongDTO.year.notempty}")
    //@Past(message = "{createSongDTO.year.past}")
    private String year;
}
