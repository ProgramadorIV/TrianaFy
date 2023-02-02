package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.validation.annotation.UniqueArtistName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder
public class CreateArtistDTO {

    @NotEmpty(message = "{createArtistDTO.name.notempty}")
    @UniqueArtistName
    private String name;
}
