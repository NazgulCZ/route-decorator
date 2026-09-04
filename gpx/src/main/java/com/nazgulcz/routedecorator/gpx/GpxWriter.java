package com.nazgulcz.routedecorator.gpx;

import com.nazgulcz.routedecorator.model.Point;
import com.nazgulcz.routedecorator.model.Route;
import com.nazgulcz.routedecorator.model.Waypoint;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes decorated routes back to GPX format.
 */
public final class GpxWriter {

    private GpxWriter() {
        // Utility
    }

    public static void write(Route route, Path outputPath) throws GpxException {
        try (OutputStream out = Files.newOutputStream(outputPath)) {
            write(route, out);
        } catch (IOException e) {
            throw new GpxException("Failed to write output file: " + e.getMessage(), e);
        }
    }

    public static void write(Route route, OutputStream out) throws GpxException {
        try {
            GpxRoute gpxRoute = convertRoute(route);
            GpxDocument doc = new GpxDocument();
            doc.setRoutes(List.of(gpxRoute));

            JAXBContext context = JAXBContext.newInstance(GpxDocument.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(doc, out);
        } catch (JAXBException e) {
            throw new GpxException("Failed to write output file: " + e.getMessage(), e);
        }
    }

    private static GpxRoute convertRoute(Route route) {
        GpxRoute gpxRoute = new GpxRoute();
        gpxRoute.setName(route.getName());

        List<GpxPoint> gpxPoints = new ArrayList<>();
        for (Point point : route.getPoints()) {
            GpxPoint gpxPoint = new GpxPoint();
            gpxPoint.setLat(point.getLatitude());
            gpxPoint.setLon(point.getLongitude());
            if (point.getElevation() != 0.0) {
                gpxPoint.setEle(point.getElevation());
            }
            gpxPoints.add(gpxPoint);
        }

        gpxRoute.setPoints(gpxPoints);
        return gpxRoute;
    }
}
