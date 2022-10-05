package com.patapchuk.song.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.patapchuk.song.model.DTO.SongRequest;
import com.patapchuk.song.model.Song;
import com.patapchuk.song.service.SongService;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1/songs")
public record SongController(SongService songService) {

    @PostMapping
    public ResponseEntity<Map<String, String>> createSong(@RequestBody SongRequest songRequest) {
        log.info("New song registration referencing to storage audio/mpeg, {}", songRequest.toString());
        return ResponseEntity.ok()
                .body(Collections.singletonMap("id", songService.create(songRequest)
                        .getId()
                        .toString()));
    }

    @GetMapping("{id}")
    public ResponseEntity<Song> getSong(@PathVariable Long id) {
        log.info("Get song by ID = {}", id);
        return ResponseEntity.ok()
                .body(songService.find(id));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, List<Long>>> deleteSongByResourceId(@RequestParam("ids") @NotNull String ids) {
        log.info("Song deletion if ids exists = {}", ids);
       return ResponseEntity.ok()
                .body(Collections.singletonMap("ids", songService.delete(ids)));
    }

}
