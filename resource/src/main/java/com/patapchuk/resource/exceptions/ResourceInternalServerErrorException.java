package com.patapchuk.resource.exceptions;

import java.io.Serial;

public class ResourceInternalServerErrorException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7758780362123376311L;

    public ResourceInternalServerErrorException(String message) {
        super(message);
    }
}
