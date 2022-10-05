package com.patapchuk.song.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.patapchuk.song.model.Song;

public interface SongRepository extends JpaRepository<Song, Long> {

}
