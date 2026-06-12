package com.nazgulcz.routedecorator.cli;

/**
 * Exception for CLI-specific errors.
 */
public class CliException extends Exception {
    public CliException(String message) {
        super(message);
    }

    public CliException(String message, Throwable cause) {
        super(message, cause);
    }
}
