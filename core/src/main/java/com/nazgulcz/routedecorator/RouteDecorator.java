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
     * Create a RouteDecorator with default polygon radius of 20 meters.
     */
    public RouteDecorator() {
        this(20.0);
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
     *
     * The insertion sequence is 8 points:
     *  1) connector from center (route point closest to waypoint) toward the first hex vertex
     *  2) six hexagon vertices (in generator order)
     *  3) connector back to the center (the center point)
     */
    private void addHexagonToRoute(Route route, Waypoint waypoint) {
        // Find the route point closest to the waypoint - this is the polygon center on the route
        int centerIndex = findClosestRoutePointIndex(route.getPoints(), waypoint.getPoint());
        Point center = route.getPoints().get(centerIndex);

        // Generate hexagon around the center point (must return exactly 6 vertices)
        List<Point> hexagonPoints = GeometryUtils.generateHexagon(center, polygonRadiusInMeters);

        // First connector: midpoint from center to first hex vertex
        Point firstConnector = hexagonPoints.get(5);// midpoint(center, firstVertex);

        // Last connector: return to the center point (duplicate of center)
        Point lastConnector = center;//new Point(center.getLatitude(), center.getLongitude(), center.getElevation());

        // Build insertion list: 1 connector + 6 hex vertices + 1 connector back
        List<Point> toInsert = new ArrayList<>(8);
        toInsert.add(firstConnector);
        toInsert.addAll(hexagonPoints);
        toInsert.add(lastConnector);

        // Insert after the center index so the polygon branches out from the route at the center point
        int insertionIndex = Math.min(route.getPoints().size(), centerIndex + 1);
        for (int i = 0; i < toInsert.size(); i++) {
            route.getPoints().add(insertionIndex + i, toInsert.get(i));
        }
    }

    private int findClosestRoutePointIndex(List<Point> routePoints, Point waypoint) {
        double minDistance = Double.MAX_VALUE;
        int bestIndex = 0;
        for (int i = 0; i < routePoints.size(); i++) {
            double d = waypoint.distanceTo(routePoints.get(i));
            if (d < minDistance) {
                minDistance = d;
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    private Point midpoint(Point a, Point b) {
        double lat = (a.getLatitude() + b.getLatitude()) / 2.0;
        double lon = (a.getLongitude() + b.getLongitude()) / 2.0;
        double ele = (a.getElevation() + b.getElevation()) / 2.0;
        return new Point(lat, lon, ele);
    }
}
