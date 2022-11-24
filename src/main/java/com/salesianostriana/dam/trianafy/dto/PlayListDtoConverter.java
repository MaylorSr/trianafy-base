package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import org.springframework.stereotype.Component;

@Component
public class PlayListDtoConverter {

    public Playlist createPlayListDtoToPlayList(CreatePlayListDto p) {
        return new Playlist(
                p.getId(),
                p.getName(),
                p.getDescription()
        );
    }

    public PlayListResponse PlayListToPlayListResponse(Playlist p) {
        return PlayListResponse
                .builder()
                .id(p.getId())
                .name(p.getName())
                .numberOfSongs(p.getSongs().size())
                .build();
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
