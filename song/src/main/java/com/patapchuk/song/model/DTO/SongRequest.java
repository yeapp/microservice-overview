package com.patapchuk.song.model.DTO;

import javax.validation.constraints.NotNull;

public record SongRequest(@NotNull String name,
                          @NotNull String artist,
                          @NotNull String album,
                          @NotNull String length,
                          @NotNull(message = "Validation error, missing resourceId") String resourceId,
                          @NotNull String year) {
}
