package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;
    private final SongService songService;

    @Operation(summary = "Obtienes una lista de todos los artistas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artistas encontrados",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                            {"id": 1, "name": "Anuel AA"},
                                            {"id": 2, "name": "Rome Santos"}                  
                                            ]
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado ninguna lista",
                    content = @Content)})
    @GetMapping("/")
    public ResponseEntity<List<Artist>> findAll() {
        if (artistService.findAll().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(artistService.findAll());

    }

    @Operation(summary = "Obtiene un artista por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class),
                            examples = {@ExampleObject(
                                    value = """                                            
                                            {"id": 1, "name": "Anuel AA"},
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado nigun artista",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Artist> findByID(@PathParam("ID") @Parameter(description = "Poner el ID del artista") @PathVariable Long id) {
        if (!artistService.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(artistService.findById(id));
    }

    @Operation(summary = "Añade un nuevo artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Artista Creado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class),
                            examples = {@ExampleObject(
                                    value = """                                            
                                            {"id": 1, "name": "Anuel AA"},
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400", description = "No se han introducido de manera correcta los datos al guardar un artista",
                    content = @Content),
    })

    @PostMapping("/")
    public ResponseEntity<Artist> addArtist(
            @RequestBody Artist artist) {

        if (artist.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(artistService.add(artist));
    }


    @Operation(summary = "Edita un artista por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista Editado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class),
                            examples = {@ExampleObject(
                                    value = """                                            
                                            {"id": 1, "name": "Anuel AA"},
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400", description = "No se ha encontrado este artista",
                    content = @Content),
    })

    @PutMapping("/{id}")
    public ResponseEntity<Artist> editArtist(@RequestBody Artist artist, @PathVariable Long id) {
        if (!artistService.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(artistService.findById(id).map(old -> {
            old.setName(artist.getName());
            artistService.edit(artist);
            return old;
        }));
    }

    @Operation(summary = "Elimina un artista por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Artista eliminado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class)
                    )})
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathParam("ID") @Parameter(description = "Poner el ID del artista a eliminar") @PathVariable Long id) {
        if (artistService.findById(id).isPresent()) {
            Artist a = artistService.findById(id).orElse(null);
            songService.findAll()
                    .stream().filter(song -> song.getArtist().equals(a)).forEach(song -> song.setArtist(null));
            artistService.deleteById(id);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
