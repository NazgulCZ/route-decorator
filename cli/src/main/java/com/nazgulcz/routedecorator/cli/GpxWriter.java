package com.nazgulcz.routedecorator.cli;

import com.nazgulcz.routedecorator.gpx.GpxWriter;
import com.nazgulcz.routedecorator.model.Point;
import com.nazgulcz.routedecorator.model.Route;
import jakarta.xml.bind.JAXBException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
            GpxWriter.write(route, outputPath);
        } catch (Exception e) {
            throw new CliException("Failed to write output file: " + e.getMessage(), e);
        }
    }

    // kept the original conversion logic previously used by the old writer is now performed inside gpx.GpxWriter
}
