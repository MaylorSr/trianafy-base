package com.salesianostriana.dam.trianafy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSongDto {
    private String title, album;
    @Column(name = "year_of_song")
    private String year;
    private Long artistId;


}
