package com.salesianostriana.dam.trianafy.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.salesianostriana.dam.trianafy.dto.CreateSongDto;
import com.salesianostriana.dam.trianafy.dto.SongDtoConverter;
import com.salesianostriana.dam.trianafy.dto.SongResponse;
import com.salesianostriana.dam.trianafy.dto.SongViewDto;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import interfaces.ViewSong;
import interfaces.ViewSongCreate;
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
import org.springframework.web.servlet.View;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
public class SongController {

    private final ArtistService artistService;
    private final SongService songService;
    private final SongDtoConverter dtoConverter;

    private final SongViewDto dtoConverter2;

    @Operation(summary = "Obtienes una lista de todos las canciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canciones encontrados",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                            {"id": 1, "title": "Ahora dice (remix)", 
                                            "album": "Ahora dice (remix)", "year":"2020",
                                            "artist": {"id": 1,"name": "Anuel AA, JBalvin"}"},
                                             
                                            {"id": 2, "title": "Mi Casa", 
                                            "album": "Mi Casa", "year":"2020",
                                            "artist": {"id": 2,"name": "Cruz Cafuné"}"}
                                                                                 
                                            ]
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado ninguna lista",
                    content = @Content)})

    @GetMapping("/")
    @JsonView(ViewSong.SongResponse.class)
    public ResponseEntity<List<SongViewDto>> findAll() {
        if (songService.findAll().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<SongViewDto> listSongs = songService.findAll().stream().map(dtoConverter2::songToSongResponse).collect(Collectors.toList());
        return ResponseEntity.ok(listSongs);

    }

    @Operation(summary = "Obtiene una canción por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canción encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class),
                            examples = {@ExampleObject(
                                    value = """                                            
                                            {"id": 2, "title": "Mi Casa", 
                                            "album": "Mi Casa", "year":"2020",
                                            "artist": {"id": 2,"name": "Cruz Cafuné"}"}
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado nignua canción",
                    content = @Content),
    })

    @GetMapping("/{id}")
    @JsonView(ViewSong.SongResponse.class)
    public ResponseEntity<SongViewDto> findById(@PathParam("ID") @Parameter(description = "Poner el ID de la canción") @PathVariable Long id) {
        if (songService.findById(id).isPresent()) {
            return ResponseEntity.ok(dtoConverter2.songToSongResponse(songService.findById(id).get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Elimina una canción por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Canción eliminada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class),
                            examples = {@ExampleObject(
                                    value = """                                            
                                            {""}
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado nignua canción",
                    content = @Content),
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathParam("ID") @Parameter(description = "Poner el ID de la canción a eliminar") @PathVariable Long id) {
        if (songService.findById(id).isPresent()) {
            songService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @Operation(summary = "Añadir una canción ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Canción creada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class),
                            examples = {@ExampleObject(
                                    value = """                                            
                                            {"id": 2, "title": "Mi Casa", 
                                            "album": "Mi Casa", "year":"2020",
                                            "artist": {"id": 2,"name": "Cruz Cafuné"}"}
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400", description = "No se ha introducido los datos de manera correcta",
                    content = @Content),
    })
    @PostMapping("/")
    @JsonView(ViewSongCreate.CreateSongDto.class)
    public ResponseEntity<SongViewDto> create(@RequestBody SongViewDto dto) {
        if (dto.getArtistId() != null && dto.getTitle() != "" && dto.getAlbum() != "") {
            Song nuevo = dtoConverter.createSongDtoToSong(dto);
            Artist artist = artistService.findById(dto.getArtistId()).get();
            nuevo.setArtist(artist);
            songService.add(nuevo);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(songService.add(nuevo));
        }
        return ResponseEntity.badRequest().build();
    }


    @Operation(summary = "Editar una canción por su ID ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canción editada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class),
                            examples = {@ExampleObject(
                                    value = """                                            
                                            {"id": 2, "title": "Mi Casa", 
                                            "album": "Mi Casa", "year":"2020",
                                            "artist": {"id": 2,"name": "Cruz Cafuné"}"}
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400", description = "No se ha introducido los datos de manera correcta",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado ninguna canción",
                    content = @Content)
    })

    /**
     * FALTA EL EDITAR DE SONG, NO SE EDITA NO SÉ PORQUÉ
     * @param song
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<SongViewDto> editSong(@PathParam("ID") @Parameter(description = "Poner el ID de la canción a editar")
                                                @RequestBody Song song, @PathVariable Long id) {

        Song data = songService.findById(id).get();

        if (data.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (data.getId() != null && data.getTitle() != "" && data.getAlbum() != "") {
            return ResponseEntity.ok().body(dtoConverter2.songToSongResponse(song));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

}
