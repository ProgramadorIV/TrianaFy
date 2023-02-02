package com.salesianostriana.dam.trianafy.model;


import com.salesianostriana.dam.trianafy.validation.annotation.UniqueArtistName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Data
@Builder
public class Artist {

    public Artist(String name){
        this.name = name;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

}
