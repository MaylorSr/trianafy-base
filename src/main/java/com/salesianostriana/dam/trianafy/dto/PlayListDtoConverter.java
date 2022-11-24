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
        PlayListResponse result = new PlayListResponse();
        result.setId(p.getId());
        result.setName(p.getName());
        result.setNumberOfSongs(p.getSongs().size());
        return result;
    }

}
