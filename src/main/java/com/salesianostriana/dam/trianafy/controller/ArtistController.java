package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artist;

import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repos.ArtistRepository;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;
    private final SongService songService;
    private final ArtistRepository artistRepository;

    @GetMapping("/")
    public ResponseEntity<List<Artist>> findAll() {
        if (artistService.findAll().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(artistService.findAll());

    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> findByID(@PathVariable Long id) {
        if (!artistService.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(artistService.findById(id));
    }

    @PostMapping("/")
    public ResponseEntity<Artist> addArtist(@RequestBody Artist artist) {

        if (artist.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(artistService.add(artist));
    }

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

    /**
     * @param id
     * @return
     * @DeleteMapping("/{id}") public ResponseEntity<List<Song>> delete(@PathVariable Long id) {
     * if (artistService.findById(id).isPresent()) {
     * return songService.findAll();
     * }
     * <p>
     * /*
     * return ResponseEntity.noContent().build();
     * <p>
     * }
     */


}
