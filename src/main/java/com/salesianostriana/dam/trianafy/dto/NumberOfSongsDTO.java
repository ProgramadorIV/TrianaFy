package com.salesianostriana.dam.trianafy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//[{ “id”: 1, “name”: “The name”, “numberOfSongs”: 7 }, …]
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class NumberOfSongsDTO {

    private Long id;
    private String name;
    private int numberOfSongs;
}
