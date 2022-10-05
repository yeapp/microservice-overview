package com.patapchuk.song.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.patapchuk.song.exceptions.SongNotFoundException;
import com.patapchuk.song.model.DTO.SongRequest;
import com.patapchuk.song.model.Song;
import com.patapchuk.song.repository.SongRepository;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public record SongService(SongRepository songRepository, RestTemplate restTemplate) {

    public Song create(SongRequest songRequest) {
        return songRepository.saveAndFlush(Song.builder()
                .name(songRequest.name())
                .artist(songRequest.artist())
                .album(songRequest.album())
                .length(songRequest.length())
                .resourceId(songRequest.resourceId())
                .year(songRequest.year())
                .build());
    }

    public Song find(Long id) {
        Optional<Song> song = songRepository.findById(id);
        if (song.isEmpty() || song.get().getResourceId().isEmpty()) {
            throw new SongNotFoundException(String.format("The resource doesn't exist with given song id %s", id));
        }
        return song.get();
    }

    public List<Long> delete(String ids) {
        List<Long> idsList = Arrays
                .stream(ids.split(","))
                .map(s -> Long.parseLong(s.trim()))
                .toList();

        List<Long> deletedResources = new LinkedList<>();
        for (Long id : idsList) {
            if (songRepository.existsById(id)) {
                songRepository.deleteById(id);
                deletedResources.add(id);
            }
        }
        return deletedResources;
    }
}
