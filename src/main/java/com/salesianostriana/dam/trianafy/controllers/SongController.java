package com.salesianostriana.dam.trianafy.controllers;

import com.salesianostriana.dam.trianafy.dto.CreateSongDTO;
import com.salesianostriana.dam.trianafy.dto.GetSongDTO;
import com.salesianostriana.dam.trianafy.dto.SongDTOConverter;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/song")
public class SongController {

    private final SongService songService;
    private final SongDTOConverter songDTOConverter;
    private final ArtistService artistService;
    private final PlaylistService playlistService;

    @GetMapping("/")
    public ResponseEntity<List<GetSongDTO>> getAllSongs(){
        List<Song> songList = songService.findAll();

        if(songList.isEmpty())
            return ResponseEntity.notFound().header("404","There are no available songs.").build();
        else{
            return ResponseEntity.ok().body(songList
                    .stream()
                    .map(songDTOConverter::SongToGetSongDTO)
                    .collect(Collectors.toList())
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id){
        List<Song> songList = songService.findAll();

        if(songList.isEmpty()){
            return ResponseEntity.notFound().header("404", "There are no available songs.").build();
        }
        for(Song song: songList){
            if(song.getId() == id)
                return ResponseEntity.of(songService.findById(id));
        }
        return ResponseEntity.notFound().header("404", "This song does not exist.").build();
    }

    @PostMapping("/")
    public ResponseEntity<GetSongDTO> createSong(@RequestBody CreateSongDTO createSongDTO){

        Song newSong = songDTOConverter.createSongDTOToSong(createSongDTO);
        Artist artist = artistService
                .findById(createSongDTO.getArtistId())
                .isPresent()?
                artistService.findById(createSongDTO.getArtistId()).get() : null;

        newSong.setArtist(artist);
        songService.add(newSong);
        return ResponseEntity.status(HttpStatus.CREATED).body(songDTOConverter.SongToGetSongDTO(newSong));
    }

    @PutMapping("/{id}") //getDTO
    public ResponseEntity<GetSongDTO> updateSong(@PathVariable Long id, @RequestBody CreateSongDTO createSongDTO){

        Optional<Song> song = songService.findById(id);

        if(song.isPresent()){
            Song newSong = songDTOConverter.createSongDTOToSong(createSongDTO);
            Artist artist = artistService
                    .findById(createSongDTO.getArtistId())
                    .isPresent()?
                    artistService.findById(createSongDTO.getArtistId()).get() : null;

            newSong.setArtist(artist);

            songService.add(
                    song.map(s ->{
                        s.setTitle(newSong.getTitle());
                        s.setAlbum(newSong.getAlbum());
                        s.setYear(newSong.getYear());
                        s.setArtist(newSong.getArtist());
                        return s;
                    }).get()
            );

            return ResponseEntity.of(Optional.of(songDTOConverter.SongToGetSongDTO(song.get())));
        }
        return ResponseEntity.notFound().header("404", "This song does not exist.").build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable Long id){

        if(songService.findById(id).isPresent()){

            List<Playlist> playlists = playlistService.findAll();

            if(playlists.isEmpty()){
                songService.deleteById(id);
                return ResponseEntity.noContent().header("204", "The song has been removed successfully.").build();
            }
            else{
                for(Playlist playlist: playlists){
                    playlist.setSongs(playlist.getSongs()
                            .stream()
                            .filter(song -> song.getId() != id)
                            .collect(Collectors.toList()));
                }
                songService.deleteById(id);
                return ResponseEntity.noContent().header("204", "The song has been removed successfully.").build();
            }

        }
        return ResponseEntity.notFound().header("404", "This song does not exist.").build();
    }



}
