package com.salesianostriana.dam.trianafy.controllers;

import com.salesianostriana.dam.trianafy.dto.*;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
import com.salesianostriana.dam.trianafy.service.SongService;
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

    private final SongService songService;

    @GetMapping("/")
    public ResponseEntity<List<NumberOfSongsDTO>> getAllPlayLists(){

        List<Playlist> playlists = playlistService.findAll();

        if(playlists.isEmpty())
            return ResponseEntity.notFound().header("404", "There are no available playlists.").build();
        else{
            List<NumberOfSongsDTO> result = playlists.stream()
                    .map(playlistDTOConverter::playListToNumberOfSongsDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(result);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPlaylistWithArtistDTO> getPlaylistById(@PathVariable Long id){

        List<Playlist> playlists = playlistService.findAll();

        if(playlists!=null){
            for(Playlist playlist: playlists){
                if(playlist.getId()==id)
                    return ResponseEntity.of(Optional.of(playlistDTOConverter.playlistToGetPlaylistWithArtistDTO(playlist)));
            }
        }
        else
            return ResponseEntity.notFound().header("404","This playlist does not exist..").build();

        return ResponseEntity.notFound().header("404", "There are no available playlists.").build();
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
        return ResponseEntity.notFound().header("404", "This playlist does not exist.").build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id){

        if(playlistService.findById(id).isPresent()){
            playlistService.deleteById(id);
            return ResponseEntity.noContent().header("204", "The playlist has been removed successfully.").build();
        }
        return ResponseEntity.notFound().header("404", "This playlist does not exist.").build();
    }

    @PostMapping("/list/{id1}/song/{id2}")
    public ResponseEntity<GetPlaylistWithArtistDTO> addSongToPlaylist(@PathVariable("id1") Long idList, @PathVariable("id2") Long idSong){

        Optional<Playlist> playlist = playlistService.findById(idList);
        Optional<Song> song = songService.findById(idSong);

        if(playlist.isPresent() && song.isPresent()){

            playlist.get().addSong(song.get());
            playlistService.add(playlist.get());
            GetPlaylistWithArtistDTO result = playlistDTOConverter.playlistToGetPlaylistWithArtistDTO(playlist.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        return ResponseEntity.notFound().header("404", "Some of the specified parameters do/es not exist.").build();
    }

    @GetMapping("/list/{id}/song")
    public ResponseEntity<GetPlaylistWithArtistDTO> getAllSongsFromPlaylist(@PathVariable Long id){

        Optional<Playlist> playlist = playlistService.findById(id);

        if(playlist.isPresent()){

            return ResponseEntity.of(Optional.of(playlistDTOConverter.playlistToGetPlaylistWithArtistDTO(playlist.get())));
        }
        return ResponseEntity.notFound().header("404", "This playlist does not exist.").build();
    }

    @GetMapping("/list/{id1}/song/{id2}")
    public ResponseEntity<Song> getSongFromPlaylist(@PathVariable("id1") Long listId, @PathVariable("id2") Long songId){

        Optional<Playlist> playlist = playlistService.findById(listId);
        Optional<Song> song = songService.findById(songId);

        if(playlist.isPresent() && song.isPresent()){
            for(Song s: playlist.get().getSongs()){
                if(s.getId() == songId)
                    return ResponseEntity.of(songService.findById(songId));
            }
        }

        return ResponseEntity.notFound().header("404", "Some of the specified parameters do/es not exist.").build();
    }

   @DeleteMapping("/list/{id1}/song/{id2}")
    public ResponseEntity<?> deleteSongFromPlaylist(@PathVariable("id1") Long listId, @PathVariable("id2") Long songId){
        Optional<Playlist> playlist = playlistService.findById(listId);
        Optional<Song> song = songService.findById(songId);

        if(playlist.isPresent() && song.isPresent()){
            for(Song s: playlist.get().getSongs()){
                if(s.getId()==songId){
                    playlist.get().setSongs(
                            playlist.get().getSongs()
                                    .stream()
                                    .filter(song1 -> song1.getId() != songId)
                                    .collect(Collectors.toList())
                    );
                    playlistService.add(playlist.get());
                    return ResponseEntity.noContent().header("204", "The song/s has/have been removed successfully from the playlist.").build();
                }
            }
        }

        return ResponseEntity.notFound().header("404", "Some of the specified parameters do/es not exist.").build();
    }
}
