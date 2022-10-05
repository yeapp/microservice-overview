package com.patapchuk.song.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Song {
    @Id
    @SequenceGenerator(
            name = "song_id_sequence",
            sequenceName = "song_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "song_id_sequence"
    )
    private Long Id;
    @NotNull
    private String resourceId;
    private String name;
    private String artist;
    private String album;
    private String length;
    private String year;
}
