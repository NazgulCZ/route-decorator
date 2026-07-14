package com.nazgulcz.routedecorator.geometry;

import com.nazgulcz.routedecorator.model.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for geometric calculations.
 */
public class GeometryUtils {

    private GeometryUtils() {
        // Utility class - no instantiation
    }

    /**
     * Find the nearest point on a polyline (sequence of connected points) to a given point.
     *
     * @param waypoint the point to find the nearest point for
     * @param routePoints the points forming the polyline
     * @return the nearest point on the polyline
     */
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

        // Check distance to each segment of the polyline
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

    /**
     * Find the nearest point on a line segment to a given point.
     *
     * @param point the point to find the nearest point for
     * @param segmentStart the start of the line segment
     * @param segmentEnd the end of the line segment
     * @return the nearest point on the segment
     */
    public static Point findNearestPointOnSegment(Point point, Point segmentStart, Point segmentEnd) {
        double dx = segmentEnd.getLongitude() - segmentStart.getLongitude();
        double dy = segmentEnd.getLatitude() - segmentStart.getLatitude();

        if (dx == 0 && dy == 0) {
            return segmentStart;
        }

        // Calculate the projection of the point onto the line segment
        double t = ((point.getLongitude() - segmentStart.getLongitude()) * dx +
                    (point.getLatitude() - segmentStart.getLatitude()) * dy) /
                   (dx * dx + dy * dy);

        // Clamp t to [0, 1] to stay on the segment
        t = Math.max(0, Math.min(1, t));

        double projLon = segmentStart.getLongitude() + t * dx;
        double projLat = segmentStart.getLatitude() + t * dy;

        return new Point(projLat, projLon);
    }

    /**
     * Generate a regular hexagon around a center point with the given radius (in meters).
     * Uses equidistant points from the center to form a regular hexagon.
     *
     * @param center the center point
     * @param radiusInMeters the radius of the hexagon in meters (distance from center to each vertex)
     * @return a list of 7 points forming a closed hexagon (first point is repeated at the end)
     */
    public static List<Point> generateHexagon(Point center, double radiusInMeters) {
        Objects.requireNonNull(center, "Center cannot be null");
        if (radiusInMeters <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }

        List<Point> hexagon = new ArrayList<>();
        
        // Conversion factor: 1 degree latitude ≈ 111 km
        // For longitude, we adjust by the cosine of the latitude to account for convergence at poles
        double radiusInDegreesLat = radiusInMeters / 111000.0;
        double radiusInDegreesLon = radiusInDegreesLat / Math.cos(Math.toRadians(center.getLatitude()));

        // Generate 6 vertices of a regular hexagon, starting from the north (top) and going clockwise
        for (int i = 0; i < 6; i++) {
            double angle = (i * 60) * Math.PI / 180.0; // 60 degrees apart
            
            // Calculate offsets using sin and cos relative to the north direction
            double offsetLat = radiusInDegreesLat * Math.cos(angle);
            double offsetLon = radiusInDegreesLon * Math.sin(angle);

            double lat = center.getLatitude() + offsetLat;
            double lon = center.getLongitude() + offsetLon;

            hexagon.add(new Point(lat, lon, center.getElevation()));
        }

        // Add the first point again to close the hexagon
        hexagon.add(hexagon.get(0));

        return hexagon;
    }
}
