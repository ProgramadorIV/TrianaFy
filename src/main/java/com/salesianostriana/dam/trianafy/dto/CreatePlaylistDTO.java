package com.salesianostriana.dam.trianafy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//{ “id”: 1, “name”: “The name”, “description”: “The desc” }
@Data @AllArgsConstructor @NoArgsConstructor
public class CreatePlaylistDTO {

    private String name;
    private String description;
}
