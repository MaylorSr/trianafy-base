package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Song;
import org.springframework.stereotype.Component;


@Component
public class SongDtoConverter {

    public Song createSongDtoToSong(CreateSongDto s) {
        return new Song(
                s.getTitle(),
                s.getAlbum(),
                s.getYear()
        );
    }


    public SongResponse songToSongResponse(Song song) {
        SongResponse result = new SongResponse();
        result.setTittle(song.getTitle());
        result.setAlbum(song.getAlbum());
        result.setYear(song.getYear());
        result.setArtist(song.getArtist().getName());
        return result;


    }
}
