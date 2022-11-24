package com.salesianostriana.dam.trianafy.repos;

import com.salesianostriana.dam.trianafy.dto.PlayListResponse2;
import com.salesianostriana.dam.trianafy.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

}
