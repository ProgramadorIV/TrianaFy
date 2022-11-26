package com.salesianostriana.dam.trianafy.controllers;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
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
    private final SongService songService;

    @PostMapping("/") //400
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(artistService.add(artist));
    }

    @GetMapping("/") //404
    public ResponseEntity<List<Artist>> getAllArtist(){
        if(artistService.findAll().isEmpty())
            return ResponseEntity.notFound().build();
        else{
            return ResponseEntity.ok().body(artistService.findAll());
        }
    }

    @GetMapping("/{id}") //404
    public ResponseEntity<Artist> getArtistById(@PathVariable Long id){
        List<Artist> lista = getAllArtist().getBody();

        try {
            for (Artist artist: lista) {
                if(artist.getId() == id)
                    return ResponseEntity.of(artistService.findById(id));
            }
        }
        catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}") //404
    public ResponseEntity<Artist> updateArtist(@PathVariable Long id, @RequestBody Artist artist){
        if(artistService.findById(id).isPresent())
            return ResponseEntity.of(
                    artistService.findById(id)
                            .map(art -> {
                                art.setName(artist.getName());
                                return Optional.of(artistService.add(art));
                            }).orElse(Optional.empty())
            );

        return ResponseEntity.notFound().build();

    }

    //No va porq el artista esta asociado a las canciones.
    @DeleteMapping("/{id}") //404
    public ResponseEntity<?> deleteArtist(@PathVariable Long id){

        if(artistService.findById(id).isPresent()){
            List<Song> listaCanciones = songService.findAll();

           if(!listaCanciones.isEmpty()){
               for(Song song: listaCanciones){
                   if(song.getArtist() != null){
                       if(song.getArtist().getId() == id) {
                           song.setArtist(null);
                           songService.add(song);
                       }
                   }
               }
               artistService.deleteById(id);
               return ResponseEntity.noContent().build();
           }
           else{
               artistService.deleteById(id);
               return ResponseEntity.noContent().build();
           }
        }

        return ResponseEntity.notFound().build();
    }
}
