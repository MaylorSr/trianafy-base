package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.CreatePlayListDto;
import com.salesianostriana.dam.trianafy.dto.PlayListDtoConverter;
import com.salesianostriana.dam.trianafy.dto.PlayListResponse;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
public class PlayListController {

    private final PlaylistService playlistService;

    private final SongService songService;
    private final PlayListDtoConverter dtoConverter;


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

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> findById(@PathVariable Long id) {
        if (playlistService.findById(id).isPresent()) {
            return ResponseEntity.ok(playlistService.findById(id).orElse(null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Playlist> delete(@PathVariable Long id) {
        if (playlistService.findById(id).isPresent()) {
            playlistService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<Playlist> editPlayList(@RequestBody PlayListResponse dto, @PathVariable Long id) {
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
    @GetMapping("/{id}/song")
    public ResponseEntity<List<Song>> findAll(@PathVariable Long id) {
        if (playlistService.findById(id).isPresent()) {
            Playlist listSong = playlistService.findById(id).orElse(null);
            return ResponseEntity.ok(listSong.getSongs());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * De una lista, obtener datos de una cancion en concreto


     @GetMapping("/{id1}/song/{id2}") public ModelAndView findByIds(@PathVariable Long id1, Long id2) {
     if (playlistService.findById(id1).isPresent()) {
     Song sonSelected = songService.findById(id2).orElse(null);
     return ModelAndView();

     }
     return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
     }
     */

}
