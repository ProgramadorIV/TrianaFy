package com.salesianostriana.dam.trianafy.controllers;

import com.salesianostriana.dam.trianafy.dto.*;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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

@RestController @RequiredArgsConstructor @RequestMapping("/playlist")
@Tag(name = "Playlist", description = "Controlador de los endpoints de playlist.")
public class PlayListController {

    private final PlaylistService playlistService;
    private final PlaylistDTOConverter playlistDTOConverter;

    private final SongDTOConverter songDTOConverter;
    private final SongService songService;

    @GetMapping("/")
    @Operation(summary = "Devuelve todas las playlists.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlists devueltas.",
                    content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema( schema = @Schema(implementation = NumberOfSongsDTO.class)))
            }),
            @ApiResponse(responseCode = "404", description = "No hay playlists disponibles.", content = @Content)
    })
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
    @Operation(summary = "Busca y devuelve una playlist por id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist encontrada.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetPlaylistWithArtistDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrada.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No hay playlists disponibles.", content = @Content)
    })
    public ResponseEntity<GetPlaylistWithArtistDTO> getPlaylistById(
            @Parameter(description = "ID de la playlist buscada", required = true)
            @PathVariable Long id
    ){

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
    @Operation(summary = "Crea una playlist.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nueva playlist", required = true,
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = CreatePlaylistDTO.class))}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Playlist modificada.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetPlaylistDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrada.",
                    content = @Content)
    })
    public ResponseEntity<GetPlaylistDTO> createPlaylist(@RequestBody CreatePlaylistDTO createPlaylistDTO){

        Playlist playlist = playlistDTOConverter.createPlaylistDTOToPlaylist(createPlaylistDTO);
        playlistService.add(playlist);

        return ResponseEntity.status(HttpStatus.CREATED).body(playlistDTOConverter.playlistToGetPlaylistDTO(playlist));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Busca y modifica una canci??n por id.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nueva playlist", required = true,
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreatePlaylistDTO.class))}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canci??n modificada.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = NumberOfSongsDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Canci??n no encontrada.",
                    content = @Content)
    })
    public ResponseEntity<NumberOfSongsDTO> updatePlaylist(
            @Parameter(description = "ID de la playlist a modificar.", required = true)
            @PathVariable Long id, @RequestBody CreatePlaylistDTO createPlaylistDTO){

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
    @Operation(summary = "Busca y elimina una canci??n por id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Canci??n eliminada.",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Canci??n no encontrada.",
                    content = @Content)
    })
    public ResponseEntity<?> deletePlaylist(
            @Parameter(description = "ID de la playlist a borrar.", required = true)
            @PathVariable Long id
    ){

        if(playlistService.findById(id).isPresent()){
            playlistService.deleteById(id);
            return ResponseEntity.noContent().header("204", "The playlist has been removed successfully.").build();
        }
        return ResponseEntity.notFound().header("404", "This playlist does not exist.").build();
    }
/////////////////////////////////////////
    @PostMapping("/list/{id1}/song/{id2}")
    @Operation(summary = "Busca la canci??n y la playlist y mete la canci??n en la playlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Canci??n a??adida a la playlist.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetPlaylistWithArtistDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Alguno de los parametros especificados no se encuentran.",
                    content = @Content)
    })
    @Parameters( value = {
            @Parameter(description = "ID de la playlist buscada", required = true),
            @Parameter(description = "ID de la canci??n a a??adir", required = true)
        }
    )
    public ResponseEntity<GetPlaylistWithArtistDTO> addSongToPlaylist(
            @PathVariable("id1") Long idList, @PathVariable("id2") Long idSong
    ){

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
    @Operation(summary = "Busca y devuelve una canci??n por id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist encontrada.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetPlaylistWithArtistDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrada.",
                    content = @Content)
    })
    public ResponseEntity<GetPlaylistWithArtistDTO> getAllSongsFromPlaylist(
            @Parameter(description = "ID de la playlist buscada.", required = true)
            @PathVariable Long id
    ){

        Optional<Playlist> playlist = playlistService.findById(id);

        if(playlist.isPresent()){

            return ResponseEntity.of(Optional.of(playlistDTOConverter.playlistToGetPlaylistWithArtistDTO(playlist.get())));
        }
        return ResponseEntity.notFound().header("404", "This playlist does not exist.").build();
    }

    @GetMapping("/list/{id1}/song/{id2}")
    @Operation(summary = "Busca la canci??n y la playlist y devuelve la canci??n.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canci??n encontrada.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class))}),
            @ApiResponse(responseCode = "404", description = "Alguno de los parametros especificados no se encuentra.",
                    content = @Content)
    })
    @Parameters( value = {
            @Parameter(description = "ID de la playlist buscada", required = true),
            @Parameter(description = "ID de la canci??n buscada", required = true)
        }
    )
    public ResponseEntity<GetSongWithArtistDTO> getSongFromPlaylist(@PathVariable("id1") Long listId, @PathVariable("id2") Long songId){

        Optional<Playlist> playlist = playlistService.findById(listId);
        Optional<Song> song = songService.findById(songId);

        if(playlist.isPresent() && song.isPresent()){
            for(Song s: playlist.get().getSongs()){
                if(s.getId() == songId)
                    return ResponseEntity.of(Optional.of(songDTOConverter.songToGetSongWithArtistDTO(songService.findById(songId).get())));
            }
        }

        return ResponseEntity.notFound().header("404", "Some of the specified parameters do/es not exist.").build();
    }

   @DeleteMapping("/list/{id1}/song/{id2}")
   @Operation(summary = "Busca la playlist y la canci??n y elimina la canci??n de la playlist.")
   @ApiResponses(value = {
           @ApiResponse(responseCode = "204", description = "Canci??n eliminada de la playlist.",
                   content = {@Content(mediaType = "application/json",
                           schema = @Schema(implementation = Song.class))}),
           @ApiResponse(responseCode = "404", description = "Alguno de los parametros especificados no se encuentra.",
                   content = @Content)
   })
   @Parameters( value = {
           @Parameter(description = "ID de la playlist buscada", required = true),
           @Parameter(description = "ID de la canci??n a eliminar", required = true)
        }
   )
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
