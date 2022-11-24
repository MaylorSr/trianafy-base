package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.CreateSongDto;
import com.salesianostriana.dam.trianafy.dto.SongDtoConverter;
import com.salesianostriana.dam.trianafy.dto.SongResponse;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
public class SongController {

    private final ArtistService artistService;
    private final SongService songService;
    private final SongDtoConverter dtoConverter;

    @GetMapping("/")
    public ResponseEntity<List<Song>> findAll() {
        if (songService.findAll().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(songService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> findById(@PathVariable Long id) {
        if (songService.findById(id).isPresent()) {
            return ResponseEntity.of(songService.findById(id));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Song> delete(@PathVariable Long id) {
        if (songService.findById(id).isPresent()) {
            songService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/")
    public ResponseEntity<Song> create(@RequestBody CreateSongDto dto) {
        if (dto.getArtistId() != null && dto.getTitle() != "" && dto.getAlbum() != "") {
            Song nuevo = dtoConverter.createSongDtoToSong(dto);
            Artist artist = artistService.findById(dto.getArtistId()).orElse(null);
            nuevo.setArtist(artist);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(songService.add(nuevo));
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * FALTA EL EDITAR DE SONG
     *
     * @param song
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<SongResponse> editSong(@RequestBody Song song, @PathVariable Long id) {

        Song data = songService.findById(id).orElse(null);

        if (data.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (data.getId() != null && data.getTitle() != "" && data.getAlbum() != "") {
            return ResponseEntity.ok().body(dtoConverter.songToSongResponse(song));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

}
