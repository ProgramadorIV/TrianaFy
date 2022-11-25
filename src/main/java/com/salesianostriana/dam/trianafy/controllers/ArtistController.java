package com.salesianostriana.dam.trianafy.controllers;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.repos.ArtistRepository;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping("/")
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(artistService.add(artist));
    }

    @GetMapping("/")
    public ResponseEntity<List<Artist>> getAllArtist(){
        return ResponseEntity.ok(artistService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable Long id){
        return ResponseEntity.of(artistService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable Long id, @RequestBody Artist artist){
        if(!artistService.findById(id).isEmpty())
            return ResponseEntity.of(
                    artistService.findById(id)
                            .map(art -> {
                                art.setName(art.getName());
                                return Optional.of(artistService.edit(art));
                            }).orElse(Optional.empty())
            );

        return ResponseEntity.badRequest().build();

    }
}
