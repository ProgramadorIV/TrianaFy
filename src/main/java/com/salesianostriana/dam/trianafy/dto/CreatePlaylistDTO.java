package com.salesianostriana.dam.trianafy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

//{ “id”: 1, “name”: “The name”, “description”: “The desc” }
@Data @AllArgsConstructor @NoArgsConstructor
public class CreatePlaylistDTO {

    @NotEmpty(message = "{createPlaylistDTO.name.notempty}")
    private String name;

    private String description;
}
