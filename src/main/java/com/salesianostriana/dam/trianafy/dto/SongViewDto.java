package com.salesianostriana.dam.trianafy.dto;


import com.fasterxml.jackson.annotation.JsonView;
import com.salesianostriana.dam.trianafy.model.Song;
import interfaces.ViewSong;
import interfaces.ViewSongCreate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SongViewDto {
    @JsonView({ViewSong.SongResponse.class, ViewSongCreate.CreateSongDto.class})
    private Long id;
    @JsonView({ViewSong.SongResponse.class, ViewSongCreate.CreateSongDto.class})
    private String title, album;
    @JsonView({ViewSong.SongResponse.class, ViewSongCreate.CreateSongDto.class})
    @Column(name = "year_of_song")
    private String year;

    @JsonView(ViewSong.SongResponse.class)
    private String artist;

    private Long artistId;

    public SongViewDto createSongDtoToSong(SongViewDto s) {
        return SongViewDto
                .builder()
                .id(s.getId())
                .title(s.getTitle())
                .artist(s.getArtist())
                .album(s.getAlbum())
                .year(s.getYear())
                .build();


    }

    public SongViewDto songToSongResponse(Song song) {
        return SongViewDto
                .builder()
                .id(song.getId())
                .title(song.getTitle())
                .artist(song.getArtist().getName())
                .album(song.getAlbum())
                .year(song.getYear())
                .build();
    }


}
