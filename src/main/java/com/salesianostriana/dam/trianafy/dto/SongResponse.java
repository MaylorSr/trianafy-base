package com.salesianostriana.dam.trianafy.dto;

import com.fasterxml.jackson.annotation.JsonView;
import interfaces.ViewSong;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.View;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongResponse {
    
    private String title, album, artist;
    private String year;


}
