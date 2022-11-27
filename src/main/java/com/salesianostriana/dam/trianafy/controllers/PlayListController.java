package com.salesianostriana.dam.trianafy.controllers;

import com.salesianostriana.dam.trianafy.dto.CreatePlaylistDTO;
import com.salesianostriana.dam.trianafy.dto.GetPlaylistDTO;
import com.salesianostriana.dam.trianafy.dto.NumberOfSongsDTO;
import com.salesianostriana.dam.trianafy.dto.PlaylistDTOConverter;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController @RequiredArgsConstructor @RequestMapping("/playlist")
public class PlayListController {

    private final PlaylistService playlistService;
    private final PlaylistDTOConverter playlistDTOConverter;

    @GetMapping("/")
    public ResponseEntity<List<NumberOfSongsDTO>> getAllPlayLists(){

        List<Playlist> playlists = playlistService.findAll();

        if(playlists.isEmpty())
            return ResponseEntity.notFound().build();
        else{
            List<NumberOfSongsDTO> result = playlists.stream()
                    .map(playlistDTOConverter::playListToNumberOfSongsDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(result);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable Long id){

        List<Playlist> playlists = playlistService.findAll();

        try{
            for(Playlist playlist: playlists){
                if(playlist.getId()==id)
                    return ResponseEntity.of(playlistService.findById(id));
            }
        }
        catch(NullPointerException exception){
            return ResponseEntity.notFound().header("404","There´s no available lists yet.").build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<GetPlaylistDTO> createPlaylist(@RequestBody CreatePlaylistDTO createPlaylistDTO){

        Playlist playlist = playlistDTOConverter.createPlaylistDTOToPlaylist(createPlaylistDTO);
        playlistService.add(playlist);

        return ResponseEntity.status(HttpStatus.CREATED).body(playlistDTOConverter.playlistToGetPlaylistDTO(playlist));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NumberOfSongsDTO> updatePlaylist(@PathVariable Long id, @RequestBody CreatePlaylistDTO createPlaylistDTO){

        Optional<Playlist> playlist = playlistService.findById(id);

        if(playlist.isPresent()){

            Playlist newPlaylist = playlistDTOConverter.createPlaylistDTOToPlaylist(createPlaylistDTO);
            playlistService.add(
                    playlist.map(p ->{
                        p.setName(newPlaylist.getName());
                        p.setDescription(newPlaylist.getDescription());
                        return p;
                    }).get()
            );

            return ResponseEntity.of(Optional.of(playlistDTOConverter.playListToNumberOfSongsDTO(playlist.get())));
        }
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id){

        if(playlistService.findById(id).isPresent()){
            playlistService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }









}
