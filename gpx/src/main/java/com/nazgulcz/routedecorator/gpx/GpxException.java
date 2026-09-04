package com.nazgulcz.routedecorator.gpx;

/**
 * Exception for GPX parsing/writing errors in the gpx module.
 */
public class GpxException extends Exception {
    public GpxException(String message) {
        super(message);
    }

    public GpxException(String message, Throwable cause) {
        super(message, cause);
    }
}
