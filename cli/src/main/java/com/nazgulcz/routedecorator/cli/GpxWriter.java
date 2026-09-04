package com.nazgulcz.routedecorator.cli;

import com.nazgulcz.routedecorator.gpx.GpxException;
import com.nazgulcz.routedecorator.model.Route;

import java.nio.file.Path;

/**
 * Writes decorated routes back to GPX format (CLI adapter).
 */
public class GpxWriter {

    private GpxWriter() {
        // Utility class
    }

    /**
     * Write a route to a GPX file.
     *
     * @param route the route to write
     * @param outputPath path to the output file
     */
    public static void write(Route route, Path outputPath) throws CliException {
        try {
            // Delegate to the shared gpx module writer
            com.nazgulcz.routedecorator.gpx.GpxWriter.write(route, outputPath);
        } catch (GpxException e) {
            throw new CliException("Failed to write output file: " + e.getMessage(), e);
        }
    }
}
