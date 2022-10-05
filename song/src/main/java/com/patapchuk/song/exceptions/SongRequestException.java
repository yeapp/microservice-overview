package com.patapchuk.song.exceptions;

public class SongRequestException extends RuntimeException {

    public SongRequestException(String message) {
        super(message);
    }

    public SongRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
