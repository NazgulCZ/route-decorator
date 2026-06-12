package com.nazgulcz.routedecorator.cli;

import com.nazgulcz.routedecorator.cli.gpx.*;
import com.nazgulcz.routedecorator.model.Point;
import com.nazgulcz.routedecorator.model.Route;
import com.nazgulcz.routedecorator.model.Waypoint;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses GPX files using JAXB.
 */
public class GpxParser {

    private GpxParser() {
        // Utility class
    }

    /**
     * Parse a route from a GPX file.
     * Prefers routes over tracks. If multiple routes exist, uses the first one.
     *
     * @param filePath path to the GPX file
     * @return Route with points, or null if no route/track found
     */
    public static Route parseRoute(Path filePath) throws CliException {
        try {
            GpxDocument doc = unmarshal(filePath);

            // Try to parse route first
            if (doc.getRoutes() != null && !doc.getRoutes().isEmpty()) {
                GpxRoute gpxRoute = doc.getRoutes().get(0);
                return convertRoute(gpxRoute);
            }

            // Fall back to track
            if (doc.getTracks() != null && !doc.getTracks().isEmpty()) {
                GpxTrack gpxTrack = doc.getTracks().get(0);
                return convertTrack(gpxTrack);
            }

            return null;
        } catch (JAXBException e) {
            throw new CliException("Failed to parse route file: " + e.getMessage(), e);
        }
    }

    /**
     * Parse waypoints from a GPX file.
     *
     * @param filePath path to the GPX file
     * @return List of waypoints
     */
    public static List<Waypoint> parseWaypoints(Path filePath) throws CliException {
        try {
            GpxDocument doc = unmarshal(filePath);
            List<Waypoint> waypoints = new ArrayList<>();

            if (doc.getWaypoints() != null) {
                for (GpxWaypoint gpxWpt : doc.getWaypoints()) {
                    String name = gpxWpt.getName() != null ? gpxWpt.getName() : "Waypoint";
                    double ele = gpxWpt.getEle() != null ? gpxWpt.getEle() : 0.0;
                    Point point = new Point(gpxWpt.getLat(), gpxWpt.getLon(), ele);
                    waypoints.add(new Waypoint(name, point));
                }
            }

            return waypoints;
        } catch (JAXBException e) {
            throw new CliException("Failed to parse waypoint file: " + e.getMessage(), e);
        }
    }

    private static GpxDocument unmarshal(Path filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(GpxDocument.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (GpxDocument) unmarshaller.unmarshal(filePath.toFile());
    }

    private static Route convertRoute(GpxRoute gpxRoute) {
        List<Point> points = new ArrayList<>();

        if (gpxRoute.getPoints() != null) {
            for (GpxPoint gpt : gpxRoute.getPoints()) {
                double ele = gpt.getEle() != null ? gpt.getEle() : 0.0;
                points.add(new Point(gpt.getLat(), gpt.getLon(), ele));
            }
        }

        return new Route(gpxRoute.getName(), points);
    }

    private static Route convertTrack(GpxTrack gpxTrack) {
        List<Point> points = new ArrayList<>();

        if (gpxTrack.getSegments() != null) {
            for (GpxTrackSegment segment : gpxTrack.getSegments()) {
                if (segment.getPoints() != null) {
                    for (GpxPoint gpt : segment.getPoints()) {
                        double ele = gpt.getEle() != null ? gpt.getEle() : 0.0;
                        points.add(new Point(gpt.getLat(), gpt.getLon(), ele));
                    }
                }
            }
        }

        return new Route(gpxTrack.getName(), points);
    }
}
