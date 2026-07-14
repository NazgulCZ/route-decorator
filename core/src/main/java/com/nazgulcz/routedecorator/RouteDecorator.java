package com.nazgulcz.routedecorator;

import com.nazgulcz.routedecorator.geometry.GeometryUtils;
import com.nazgulcz.routedecorator.model.Point;
import com.nazgulcz.routedecorator.model.Route;
import com.nazgulcz.routedecorator.model.Waypoint;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Decorates GPX routes by adding small polygons (hexagons) around waypoint locations.
 * This solves the problem of map applications that cannot display routes and waypoints simultaneously.
 */
@Getter
@Setter
public class RouteDecorator {
    private double polygonRadiusInMeters;

    /**
     * Create a RouteDecorator with default polygon radius of 10 meters.
     */
    public RouteDecorator() {
        this(10.0);
    }

    /**
     * Create a RouteDecorator with a custom polygon radius.
     *
     * @param polygonRadiusInMeters the radius of the hexagons to draw around waypoints
     */
    public RouteDecorator(double polygonRadiusInMeters) {
        if (polygonRadiusInMeters <= 0) {
            throw new IllegalArgumentException("Polygon radius must be positive");
        }
        this.polygonRadiusInMeters = polygonRadiusInMeters;
    }

    /**
     * Decorate a route by adding hexagons around waypoint locations.
     * For each waypoint, finds the nearest point on the route and draws a hexagon around it.
     *
     * @param route the route to decorate
     * @param waypoints the waypoints to use for decoration
     * @return a new decorated route with hexagons added as additional points
     */
    public Route decorate(Route route, List<Waypoint> waypoints) {
        Objects.requireNonNull(route, "Route cannot be null");
        Objects.requireNonNull(waypoints, "Waypoints cannot be null");

        Route decoratedRoute = new Route(route.getName(), new ArrayList<>(route.getPoints()));

        for (Waypoint waypoint : waypoints) {
            addHexagonToRoute(decoratedRoute, waypoint);
        }

        return decoratedRoute;
    }

    /**
     * Add a hexagon around a waypoint to the route at the appropriate position.
     */
    private void addHexagonToRoute(Route route, Waypoint waypoint) {
        Point nearestPointOnRoute = GeometryUtils.findNearestPointOnPolyline(
                waypoint.getPoint(),
                route.getPoints()
        );

        // Find the index where the hexagon should be inserted
        int insertionIndex = findInsertionIndex(route.getPoints(), nearestPointOnRoute, waypoint.getPoint());

        List<Point> hexagonPoints = GeometryUtils.generateHexagon(nearestPointOnRoute, polygonRadiusInMeters);
        
        // Insert hexagon points at the correct position
        for (int i = 0; i < hexagonPoints.size(); i++) {
            route.getPoints().add(insertionIndex + i, hexagonPoints.get(i));
        }
    }

    /**
     * Find the index in the route where the hexagon should be inserted.
     * The hexagon is inserted after the segment that contains the nearest point.
     */
    private int findInsertionIndex(List<Point> routePoints, Point nearestPoint, Point waypoint) {
        double minDistance = Double.MAX_VALUE;
        int segmentIndex = 0;

        // Find which segment the nearest point is closest to
        for (int i = 0; i < routePoints.size() - 1; i++) {
            Point segmentStart = routePoints.get(i);
            Point segmentEnd = routePoints.get(i + 1);

            Point closestPointOnSegment = GeometryUtils.findNearestPointOnSegment(waypoint, segmentStart, segmentEnd);
            double distance = waypoint.distanceTo(closestPointOnSegment);

            if (distance < minDistance) {
                minDistance = distance;
                segmentIndex = i;
            }
        }

        // Insert after the end of the segment
        return segmentIndex + 1;
    }
}
