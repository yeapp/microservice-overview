package com.patapchuk.resource.exceptions;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7758780362123376382L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
