package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.CreatePlayListDto;
import com.salesianostriana.dam.trianafy.dto.PlayListDtoConverter;
import com.salesianostriana.dam.trianafy.dto.PlayListResponse;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
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

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
public class PlayListController {

    private final PlaylistService playlistService;

    private final SongService songService;
    private final PlayListDtoConverter dtoConverter;

    @Operation(summary = "Obtienes una lista de todos las playList")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PlayList encontradas",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                            {"id": 1, "name": "Los 50 éxtios mundiales", 
                                            "description": "Mola mucho", "numberOfSongs":"4"},
                                             
                                            {"id": 2, "name": "Top 50 Argentina", 
                                            "description": "Mola poco", "numberOfSongs" : "7"}
                                                                                 
                                            ]
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado ninguna lista",
                    content = @Content)})

    @GetMapping("/")
    public ResponseEntity<List<PlayListResponse>> findAll() {
        List<Playlist> data = playlistService.findAll();
        if (data.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            List<PlayListResponse> result =
                    data.stream()
                            .map(dtoConverter::PlayListToPlayListResponse)
                            .collect(Collectors.toList());

            return ResponseEntity
                    .ok()
                    .body(result);

        }
    }
    /* Comprobaba que efectivamente hay una lista nada más llamada Random

        public List<Playlist> findAll() {
        return playlistService.findAll();
        }
     */

    @Operation(summary = "Obtienes una playList por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PlayList encontradas",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {"id": 1, "name": "Los 50 éxtios mundiales", 
                                            "description": "Mola mucho", "numberOfSongs":"4"}
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado ninguna lista",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<Playlist> findById(@PathParam("ID") @Parameter(description = "Poner el ID de la playList") @PathVariable Long id) {
        if (playlistService.findById(id).isPresent()) {
            return ResponseEntity.ok(playlistService.findById(id).orElse(null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Elimina una playList por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PlayList eliminada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {}
                                            """
                            )}
                    )})
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Playlist> delete(@PathParam("ID") @Parameter(description = "Pasar el ID de la playList") @PathVariable Long id) {
        if (playlistService.findById(id).isPresent()) {
            playlistService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Añadir una playList ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "PlayList creada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {}
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400", description = " Se han ingresado mal los datos al añadir una playList",
                    content = @Content)})

    @PostMapping("/")
    public ResponseEntity<Playlist> addPlayList(@RequestBody CreatePlayListDto dto) {

        if (dto.getName() != "") {
            Playlist nuevo = dtoConverter.createPlayListDtoToPlayList(dto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(playlistService.add(nuevo));
        }
        return ResponseEntity.badRequest().build();
    }


    @Operation(summary = "Editar una playList por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha editado de manera correcta",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {"name": "Los grandes éxitos de los 50""}
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400", description = " Se han ingresado mal los datos al añadir una playList",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado esta playList",
                    content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<Playlist> editPlayList(@PathParam("ID") @Parameter(description = "Pasar el ID de la playList")
                                                 @RequestBody PlayListResponse dto,
                                                 @PathVariable Long id) {
        if (!playlistService.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (dto.getName() == "") {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }
        return ResponseEntity.of(
                playlistService.findById(id).map(p -> {
                    p.setName(dto.getName());
                    playlistService.edit(p);
                    return p;
                })
        );
    }


    /***********************************************************************************************************************************************************/

    @Operation(summary = "Obtener las canciones de una lista por su ID de lista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "No se ha encontrado la PlayList de canciones",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                            {
                                            "id": 9,
                                            "title": "Enter Sandman",
                                            "album": "Metallica",
                                            "year": "1991",
                                            "artist": {
                                            "id": 3,
                                            "name": "Metallica"
                                            }
                                            },]
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado la PlayList de canciones",
                    content = @Content)})
    @GetMapping("/{id}/song")
    public ResponseEntity<List<Song>> findAll(@PathParam("ID") @Parameter(description = "Pasar el ID de la lista") @PathVariable Long id) {
        if (playlistService.findById(id).isPresent()) {
            Playlist listSong = playlistService.findById(id).orElse(null);
            return ResponseEntity.ok(listSong.getSongs());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Obtener detalles de una canción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "No se ha encontrado la PlayList de canciones",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                                                                        
                                            {
                                                "id": 8,
                                                "title": "Love Again",
                                                "album": "Future Nostalgia",
                                                "year": "2021",
                                                "artist": {
                                                    "id": 2,
                                                    "name": "Dua Lipa"
                                                }
                                            }
                                                                                        
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado la PlayList de canciones",
                    content = @Content)})
    /**
     * De una lista, obtener datos de una cancion en concreto
     */
    @GetMapping("/{id1}/song/{id2}")
    public ResponseEntity<Song> findByIds(@PathParam("id1 id2")
                                          @Parameter(description = "Pasar el id de la lista id1 y pasar el id de la canción id2")
                                          @PathVariable("id1") Long id1,
                                          @PathVariable("id2") Long id) {
        if (playlistService.findById(id1).isPresent() && songService.findById(id).isPresent()) {
            return ResponseEntity.of(songService.findById(id));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Eliminar una canción de una lista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Se ha borrado",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {}                                                   
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado la PlayList de canciones",
                    content = @Content)})

    @DeleteMapping("/{id1}/song/{id2}")
    public ResponseEntity delete(@PathParam("id1 id2")
                                 @Parameter(description = "Pasar el id de la lista id1 y pasar el id de la canción id2")
                                 @PathVariable("id1") Long id1, @PathVariable("id2") Long id) {
        if (playlistService.findById(id1).isPresent() && songService.findById(id).isPresent()) {
            Song s = songService.findById(id).orElse(null);
            playlistService.findById(id1).get().getSongs()
                    .stream().filter(song -> song.equals(s))
                    .forEach(song -> song.setTitle(null));

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @Operation(summary = "Agregar una canción a la lista escogida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Se ha añadido correctamente",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {}                                                   
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404", description = "No se encontrado ninguna lista para añadir",
                    content = @Content)})

    @PostMapping("/{id1}/song/{id2}")
    public ResponseEntity<Playlist> addSongList(@PathVariable("id1") Long id1, @PathVariable("id2") Long id) {
        if (playlistService.findById(id1).isPresent() && songService.findById(id).isPresent()) {
            Song s = songService.findById(id).orElse(null);
            playlistService.findById(id1).get().getSongs().add(s);
            return ResponseEntity.ok(playlistService.findById(id1).orElse(null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
