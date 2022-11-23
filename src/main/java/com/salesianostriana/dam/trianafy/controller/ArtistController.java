package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artist;
<<<<<<< HEAD
import com.salesianostriana.dam.trianafy.repos.ArtistRepository;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
=======
import com.salesianostriana.dam.trianafy.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
>>>>>>> main
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
<<<<<<< HEAD
@RequestMapping("/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;
    private final ArtistRepository artistRepository;

    @GetMapping("/")
    public ResponseEntity<List<Artist>> getArtist() {
        return ResponseEntity
                .ok()
                .body(artistService.findAll());
=======
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistService artistService;


    @GetMapping("/")
    public ResponseEntity<List<Artist>> findAll() {
        if (artistService.findAll().isEmpty()) {
            return (ResponseEntity<List<Artist>>) ResponseEntity.notFound();
        }
        return ResponseEntity.ok(artistService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> findByID(@PathVariable Long id) {
        if (!artistService.findById(id).isPresent()) {
            return (ResponseEntity<Artist>) ResponseEntity.notFound();
        }
        /**
         *      return ResponseEntity.of(artistService.findById(id));
         *
         */
        return (ResponseEntity<Artist>) ResponseEntity.notFound();

>>>>>>> main

    }

}
