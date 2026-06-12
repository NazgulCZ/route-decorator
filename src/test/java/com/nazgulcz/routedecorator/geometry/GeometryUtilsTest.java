package com.nazgulcz.routedecorator.geometry;

import com.nazgulcz.routedecorator.model.Point;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeometryUtilsTest {

    @Test
    void testFindNearestPointOnPolyline() {
        List<Point> routePoints = Arrays.asList(
                new Point(0, 0),
                new Point(0, 5),
                new Point(0, 10)
        );

        Point waypoint = new Point(1, 5);
        Point nearest = GeometryUtils.findNearestPointOnPolyline(waypoint, routePoints);

        assertEquals(0, nearest.getLatitude(), 0.0001);
        assertEquals(5, nearest.getLongitude(), 0.0001);
    }

    @Test
    void testFindNearestPointOnPolylineSinglePoint() {
        List<Point> routePoints = Arrays.asList(new Point(0, 0));
        Point waypoint = new Point(1, 1);

        Point nearest = GeometryUtils.findNearestPointOnPolyline(waypoint, routePoints);

        assertEquals(0, nearest.getLatitude());
        assertEquals(0, nearest.getLongitude());
    }

    @Test
    void testFindNearestPointOnPolylineEmptyThrows() {
        Point waypoint = new Point(1, 1);

        assertThrows(IllegalArgumentException.class, () ->
                GeometryUtils.findNearestPointOnPolyline(waypoint, Arrays.asList())
        );
    }

    @Test
    void testGenerateHexagon() {
        Point center = new Point(0, 0);
        List<Point> hexagon = GeometryUtils.generateHexagon(center, 10.0);

        assertEquals(6, hexagon.size());

        double expectedDistance = center.distanceTo(hexagon.get(0));
        for (Point vertex : hexagon) {
            double distance = center.distanceTo(vertex);
            assertEquals(expectedDistance, distance, 0.0001);
        }
    }

    @Test
    void testGenerateHexagonWithElevation() {
        Point center = new Point(0, 0, 100);
        List<Point> hexagon = GeometryUtils.generateHexagon(center, 10.0);

        for (Point vertex : hexagon) {
            assertEquals(100, vertex.getElevation());
        }
    }

    @Test
    void testGenerateHexagonInvalidRadius() {
        Point center = new Point(0, 0);

        assertThrows(IllegalArgumentException.class, () ->
                GeometryUtils.generateHexagon(center, 0.0)
        );

        assertThrows(IllegalArgumentException.class, () ->
                GeometryUtils.generateHexagon(center, -5.0)
        );
    }

    @Test
    void testGenerateHexagonNullCenter() {
        assertThrows(NullPointerException.class, () ->
                GeometryUtils.generateHexagon(null, 10.0)
        );
    }

    @Test
    void testFindNearestPointNullWaypoint() {
        List<Point> routePoints = Arrays.asList(new Point(0, 0));

        assertThrows(NullPointerException.class, () ->
                GeometryUtils.findNearestPointOnPolyline(null, routePoints)
        );
    }

    @Test
    void testFindNearestPointNullRoutePoints() {
        Point waypoint = new Point(1, 1);

        assertThrows(NullPointerException.class, () ->
                GeometryUtils.findNearestPointOnPolyline(waypoint, null)
        );
    }
}
