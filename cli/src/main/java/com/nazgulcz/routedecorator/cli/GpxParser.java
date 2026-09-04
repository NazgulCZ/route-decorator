package com.nazgulcz.routedecorator.cli;

import com.nazgulcz.routedecorator.model.Point;
import com.nazgulcz.routedecorator.model.Route;
import com.nazgulcz.routedecorator.model.Waypoint;
import com.nazgulcz.routedecorator.gpx.GpxException;

import java.nio.file.Path;
import java.util.List;

/**
 * CLI-facing GpxParser that delegates to the shared gpx module.
 */
public class GpxParser {

    private GpxParser() {
        // Utility
    }

    public static Route parseRoute(Path filePath) throws CliException {
        try {
            return com.nazgulcz.routedecorator.gpx.GpxParser.parseRoute(filePath);
        } catch (GpxException e) {
            throw new CliException("Failed to parse route file: " + e.getMessage(), e);
        }
    }

    public static List<Waypoint> parseWaypoints(Path filePath) throws CliException {
        try {
            return com.nazgulcz.routedecorator.gpx.GpxParser.parseWaypoints(filePath);
        } catch (GpxException e) {
            throw new CliException("Failed to parse waypoint file: " + e.getMessage(), e);
        }
    }
}
