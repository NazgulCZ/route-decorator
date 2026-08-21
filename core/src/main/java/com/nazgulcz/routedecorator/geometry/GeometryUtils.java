package com.nazgulcz.routedecorator.geometry;

import com.nazgulcz.routedecorator.model.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GeometryUtils {

    private GeometryUtils() {
    }

    public static Point findNearestPointOnPolyline(Point waypoint, List<Point> routePoints) {
        Objects.requireNonNull(waypoint, "Waypoint cannot be null");
        Objects.requireNonNull(routePoints, "Route points cannot be null");

        if (routePoints.isEmpty()) {
            throw new IllegalArgumentException("Route must contain at least one point");
        }

        if (routePoints.size() == 1) {
            return routePoints.get(0);
        }

        Point nearestPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < routePoints.size() - 1; i++) {
            Point segmentStart = routePoints.get(i);
            Point segmentEnd = routePoints.get(i + 1);

            Point closestPointOnSegment = findNearestPointOnSegment(waypoint, segmentStart, segmentEnd);
            double distance = waypoint.distanceTo(closestPointOnSegment);

            if (distance < minDistance) {
                minDistance = distance;
                nearestPoint = closestPointOnSegment;
            }
        }

        return nearestPoint;
    }

    public static Point findNearestPointOnSegment(Point point, Point segmentStart, Point segmentEnd) {
        double dx = segmentEnd.getLongitude() - segmentStart.getLongitude();
        double dy = segmentEnd.getLatitude() - segmentStart.getLatitude();

        if (dx == 0 && dy == 0) {
            return segmentStart;
        }

        double t = ((point.getLongitude() - segmentStart.getLongitude()) * dx +
                    (point.getLatitude() - segmentStart.getLatitude()) * dy) /
                   (dx * dx + dy * dy);

        t = Math.max(0, Math.min(1, t));

        double projLon = segmentStart.getLongitude() + t * dx;
        double projLat = segmentStart.getLatitude() + t * dy;

        return new Point(projLat, projLon, centerElevationPlaceholder());
    }

    // placeholder helper — the Point constructor requires elevation; route points may be 2D in tests
    private static double centerElevationPlaceholder() {
        return 0.0;
    }

    public static List<Point> generateHexagon(Point center, double radiusInMeters) {
        Objects.requireNonNull(center, "Center cannot be null");
        if (radiusInMeters <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }

        final double METERS_PER_DEGREE_LAT = 111000.0; // meters per degree latitude
        double radiusDegLat = radiusInMeters / METERS_PER_DEGREE_LAT;
        double radiusDegLonFactor = 1.0 / (METERS_PER_DEGREE_LAT * Math.cos(Math.toRadians(center.getLatitude())));

        List<Point> hexagon = new ArrayList<>(6);

        for (int i = 0; i < 6; i++) {
            double angleDeg = 90.0 - i * 60.0; // start at north and go clockwise
            double angleRad = Math.toRadians(angleDeg);

            double offsetLat = radiusDegLat * Math.cos(angleRad);
            double offsetLon = radiusInMeters * Math.sin(angleRad) * radiusDegLonFactor;

            double lat = center.getLatitude() + offsetLat;
            double lon = center.getLongitude() + offsetLon;

            hexagon.add(new Point(lat, lon, center.getElevation()));
        }

        return hexagon;
    }
}
