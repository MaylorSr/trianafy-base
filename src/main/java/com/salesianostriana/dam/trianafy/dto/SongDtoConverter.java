package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.SongService;
import org.springframework.stereotype.Component;


@Component
public class SongDtoConverter {

    private SongService songService;

    public Song createSongDtoToSong(CreateSongDto s) {
        return new Song(
                s.getTitle(),
                s.getAlbum(),
                s.getYear()
        );
    }


    public SongResponse songToSongResponse(Song song) {
        return SongResponse
                .builder()
                .title(song.getTitle())
                .artist(song.getArtist().getName())
                .album(song.getAlbum())
                .year(song.getYear())
                .build();
    }
}
