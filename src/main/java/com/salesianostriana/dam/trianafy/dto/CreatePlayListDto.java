package com.salesianostriana.dam.trianafy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlayListDto {

    @Id @GeneratedValue
    private Long id;
    private String name, description;

}
