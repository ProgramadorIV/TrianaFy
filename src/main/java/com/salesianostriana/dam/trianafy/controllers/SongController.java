package com.salesianostriana.dam.trianafy.controllers;

import com.salesianostriana.dam.trianafy.dto.*;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Song", description = "Controlador de los endpoints de canciones.")
public class SongController {

    private final SongService songService;
    private final SongDTOConverter songDTOConverter;
    private final ArtistService artistService;
    private final PlaylistService playlistService;

    @GetMapping("/")
    @Operation(summary = "Devuelve todas las canciones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canciones devueltas.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema( schema = @Schema(implementation = Song.class)))}),
            @ApiResponse(responseCode = "404", description = "No hay canciones disponibles.",
                    content = @Content)
    })
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
    @Operation(summary = "Busca y devuelve una canci??n por id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canci??n encontrada.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class))}),
            @ApiResponse(responseCode = "404", description = "Canci??n no encontrada.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No hay artistas disponibles.", content = @Content)
    })
    public ResponseEntity<GetSongWithArtistDTO> getSongById(
            @Parameter(description = "ID de la canci??n buscada.", required = true)
            @PathVariable Long id
    ){
        List<Song> songList = songService.findAll();

        if(songList.isEmpty()){
            return ResponseEntity.notFound().header("404", "There are no available songs.").build();
        }
        for(Song song: songList){
            if(song.getId() == id)
                return ResponseEntity.of(Optional.of(songDTOConverter.songToGetSongWithArtistDTO(songService.findById(id).get())));
        }
        return ResponseEntity.notFound().header("404", "This song does not exist.").build();
    }

    @PostMapping("/")
    @Operation(summary = "Crea una canci??n.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nueva canci??n", required = true,
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateSongDTO.class))}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Canci??n creada.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetSongDTO.class))}),
            @ApiResponse(responseCode = "400", description = "El formato de la canci??n no es correcto.",
                    content = @Content)
    })
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

    @PutMapping("/{id}")
    @Operation(summary = "Modifica una canci??n.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nueva playlist", required = true,
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateSongDTO.class))}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canci??n modificada.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetSongDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Canci??n no encontrada.",
                    content = @Content)
    })
    public ResponseEntity<GetSongDTO> updateSong(
            @Parameter(description = "ID de la canci??n a modificar.", required = true)
            @PathVariable Long id, @RequestBody CreateSongDTO createSongDTO
    ){

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
    @Operation(summary = "Busca y elimina una canci??n.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Canci??n eliminada.",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Canci??n no encontrada.",
                    content = @Content)
    })
    public ResponseEntity<?> deleteSong(
            @Parameter(description = "ID de la canci??n a eliminar.", required = true)
            @PathVariable Long id
    ){

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
