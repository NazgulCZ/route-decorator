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
     * Add a hexagon around a waypoint to the route.
     */
    private void addHexagonToRoute(Route route, Waypoint waypoint) {
        Point nearestPointOnRoute = GeometryUtils.findNearestPointOnPolyline(
                waypoint.getPoint(),
                route.getPoints()
        );

        List<Point> hexagonPoints = GeometryUtils.generateHexagon(nearestPointOnRoute, polygonRadiusInMeters);
        for (Point hexPoint : hexagonPoints) {
            route.addPoint(hexPoint);
        }
    }
}
