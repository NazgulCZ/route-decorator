package com.nazgulcz.routedecorator;

import com.nazgulcz.routedecorator.model.Point;
import com.nazgulcz.routedecorator.model.Route;
import com.nazgulcz.routedecorator.model.Waypoint;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RouteDecoratorTest {

    @Test
    void testDecorateRouteWithWaypoints() {
        List<Point> routePoints = Arrays.asList(
                new Point(0, 0),
                new Point(0, 5),
                new Point(0, 10)
        );
        Route route = new Route("Test Route", routePoints);

        Waypoint waypoint = new Waypoint("Waypoint 1", new Point(1, 5));
        List<Waypoint> waypoints = List.of(waypoint);

        RouteDecorator decorator = new RouteDecorator(10.0);
        Route decoratedRoute = decorator.decorate(route, waypoints);

        // Original route had 3 points, hexagon adds 8 more = 11 total
        assertEquals(11, decoratedRoute.getPointCount());
        assertEquals("Test Route", decoratedRoute.getName());
    }

    @Test
    void testDecorateRouteWithoutName() {
        List<Point> routePoints = Arrays.asList(
                new Point(0, 0),
                new Point(0, 5),
                new Point(0, 10)
        );
        Route route = new Route(routePoints);

        Waypoint waypoint = new Waypoint("Waypoint 1", new Point(1, 5));
        List<Waypoint> waypoints = List.of(waypoint);

        RouteDecorator decorator = new RouteDecorator(10.0);
        Route decoratedRoute = decorator.decorate(route, waypoints);

        assertNull(decoratedRoute.getName());
        assertEquals(11, decoratedRoute.getPointCount());
    }

    @Test
    void testMultipleWaypoints() {
        List<Point> routePoints = Arrays.asList(
                new Point(0, 0),
                new Point(0, 5),
                new Point(0, 10)
        );
        Route route = new Route(routePoints);

        List<Waypoint> waypoints = Arrays.asList(
                new Waypoint("WP1", new Point(1, 5)),
                new Waypoint("WP2", new Point(1, 10))
        );

        RouteDecorator decorator = new RouteDecorator(10.0);
        Route decoratedRoute = decorator.decorate(route, waypoints);

        // 3 original + 8 for first waypoint + 8 for second waypoint = 19
        assertEquals(19, decoratedRoute.getPointCount());
    }

    @Test
    void testCustomPolygonRadius() {
        RouteDecorator decorator = new RouteDecorator(5.0);
        assertEquals(5.0, decorator.getPolygonRadiusInMeters());

        decorator.setPolygonRadiusInMeters(20.0);
        assertEquals(20.0, decorator.getPolygonRadiusInMeters());
    }

    @Test
    void testDefaultPolygonRadius() {
        RouteDecorator decorator = new RouteDecorator();
        assertEquals(20.0, decorator.getPolygonRadiusInMeters());
    }

    @Test
    void testInvalidPolygonRadius() {
        assertThrows(IllegalArgumentException.class, () -> new RouteDecorator(-5.0));
        assertThrows(IllegalArgumentException.class, () -> new RouteDecorator(0.0));
    }

    @Test
    void testNullRoute() {
        RouteDecorator decorator = new RouteDecorator();
        List<Waypoint> waypoints = Arrays.asList();

        assertThrows(NullPointerException.class, () -> decorator.decorate(null, waypoints));
    }

    @Test
    void testNullWaypoints() {
        RouteDecorator decorator = new RouteDecorator();
        Route route = new Route(Arrays.asList(new Point(0, 0)));

        assertThrows(NullPointerException.class, () -> decorator.decorate(route, null));
    }

    @Test
    void testEmptyWaypoints() {
        List<Point> routePoints = Arrays.asList(new Point(0, 0), new Point(0, 10));
        Route route = new Route(routePoints);
        List<Waypoint> waypoints = Arrays.asList();

        RouteDecorator decorator = new RouteDecorator();
        Route decoratedRoute = decorator.decorate(route, waypoints);

        // No waypoints = no hexagons added
        assertEquals(2, decoratedRoute.getPointCount());
    }
}
