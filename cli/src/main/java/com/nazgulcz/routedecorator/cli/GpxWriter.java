package com.nazgulcz.routedecorator.cli;

import com.nazgulcz.routedecorator.cli.gpx.*;
import com.nazgulcz.routedecorator.model.Point;
import com.nazgulcz.routedecorator.model.Route;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes decorated routes back to GPX format.
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
            GpxRoute gpxRoute = convertRoute(route);
            GpxDocument doc = new GpxDocument();
            doc.setRoutes(List.of(gpxRoute));

            JAXBContext context = JAXBContext.newInstance(GpxDocument.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(doc, outputPath.toFile());
        } catch (JAXBException e) {
            throw new CliException("Failed to write output file: " + e.getMessage(), e);
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
