package com.salesianostriana.dam.trianafy.controllers;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
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


@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
@Tag(name = "Artist", description = "Controlador de los endpoints de artista.")
public class ArtistController {

    private final ArtistService artistService;
    private final SongService songService;



    @Operation(summary = "Crear un artista.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Se ha creado el artista.",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Artist.class))}),
            @ApiResponse(responseCode = "400", description = "El formato de artista no es el correcto.",
            content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(artistService.add(artist));
    }

    @GetMapping("/")
    @Operation(summary = "Devuelve todos los artistas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artistas devueltos.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class))}),
            @ApiResponse(responseCode = "404", description = "No hay artistas disponibles.",
                    content = @Content)
    })
    public ResponseEntity<List<Artist>> getAllArtist(){
        if(artistService.findAll().isEmpty())
            return ResponseEntity.notFound().header("404", "There are no available artists.").build();
        else{
            return ResponseEntity.ok().body(artistService.findAll());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca y devuelve un artista por id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista encontrado.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema( schema = @Schema(implementation = Artist.class)))}),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No hay artistas disponibles.", content = @Content)

    })
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
    @Operation(summary = "Modificar un artista.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista modificado.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class))}),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado.",
                    content = @Content),
    })
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
    @Operation(summary = "Busca el artista y lo elimina por id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Artista eliminado.",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No hay artistas disponibles.", content = @Content)
    })
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
