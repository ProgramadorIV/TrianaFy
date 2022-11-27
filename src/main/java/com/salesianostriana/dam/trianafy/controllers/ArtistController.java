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

    @PostMapping("/")
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(artistService.add(artist));
    }

    @GetMapping("/")
    public ResponseEntity<List<Artist>> getAllArtist(){
        if(artistService.findAll().isEmpty())
            return ResponseEntity.notFound().header("404", "There are no available artists.").build();
        else{
            return ResponseEntity.ok().body(artistService.findAll());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable Long id){
        List<Artist> list = getAllArtist().getBody();

        if(list==null)
            return ResponseEntity.notFound().header("404", "There are no available artist.").build();

        for (Artist artist: list) {
            if(artist.getId() == id)
                return ResponseEntity.of(artistService.findById(id));
        }

        return ResponseEntity.notFound().header("404", "This song does not exist.").build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable Long id, @RequestBody Artist artist){
        if(artistService.findById(id).isPresent())
            return ResponseEntity.of(
                    artistService.findById(id)
                            .map(art -> {
                                art.setName(artist.getName());
                                return Optional.of(artistService.add(art));
                            }).orElse(Optional.empty())
            );

        return ResponseEntity.notFound().header("404", "This song does not exist.").build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtist(@PathVariable Long id){

        if(artistService.findById(id).isPresent()){

            List<Song> songList = songService.findAll();

           if(!songList.isEmpty()){
               for(Song song: songList){
                   if(song.getArtist() != null){
                       if(song.getArtist().getId() == id) {
                           song.setArtist(null);
                           songService.add(song);
                       }
                   }
               }
           }
            artistService.deleteById(id);
            return ResponseEntity.noContent().header("404", "This song does not exist.").build();
        }

        return ResponseEntity.notFound().header("404", "There are no available songs.").build();
    }
}
