package com.patapchuk.song.exceptions;

import java.io.Serial;

public class SongNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7758780362123376381L;

    public SongNotFoundException(String message) {
        super(message);
    }
}
