package com.salesianostriana.dam.trianafy.controllers;

import com.salesianostriana.dam.trianafy.dto.CreateSongDTO;
import com.salesianostriana.dam.trianafy.dto.SongDTOConverter;
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
@RequestMapping("/song")
public class SongController {

    private final SongService songService;
    private final SongDTOConverter songDTOConverter;
    private final ArtistService artistService;

    @GetMapping("/")
    public ResponseEntity<List<Song>> getAllSongs(){
        List<Song> songList = songService.findAll();

        if(songList.isEmpty())
            return ResponseEntity.notFound().build();
        else{
            return ResponseEntity.ok().body(songList);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id){
        List<Song> songList = songService.findAll();

        try{
            for(Song song: songList){
                if(song.getId() == id)
                    return ResponseEntity.of(songService.findById(id));
            }
        }
        catch (NullPointerException exception){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/") //400
    public ResponseEntity<Song> createSong(@RequestBody CreateSongDTO createSongDTO){

        Song newSong = songDTOConverter.createSongDTOToSong(createSongDTO);
        Artist artist = artistService
                .findById(createSongDTO.getArtistId())
                .isPresent()?
                artistService.findById(createSongDTO.getArtistId()).get() : null;

        newSong.setArtist(artist);
        songService.add(newSong);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSong);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @RequestBody CreateSongDTO createSongDTO){

        Optional<Song> song = songService.findById(id);

        if(song.isPresent()){
            Song newSong = songDTOConverter.createSongDTOToSong(createSongDTO);
            Artist artist = artistService
                    .findById(createSongDTO.getArtistId())
                    .isPresent()?
                    artistService.findById(createSongDTO.getArtistId()).get() : null;

            newSong.setArtist(artist);
            return ResponseEntity.of(
                    song.map(s -> {
                        s.setTitle(newSong.getTitle());
                        s.setAlbum(newSong.getAlbum());
                        s.setYear(newSong.getYear());
                        s.setArtist(newSong.getArtist());
                        return Optional.of(songService.add(s));
                    }).orElse(Optional.empty())
            );
        }
        return ResponseEntity.notFound().build();
    }

    //No se borran canciones asociadas a playlist -> integridad referencial.

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable Long id){

        if(songService.findById(id).isPresent()){
            songService.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }



}
