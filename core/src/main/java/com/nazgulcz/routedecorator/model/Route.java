package com.nazgulcz.routedecorator.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a GPX route or track - a sequence of geographic points.
 */
@Getter
@ToString
@EqualsAndHashCode
public class Route {
    private final String name;
    private final List<Point> points;

    public Route(List<Point> points) {
        this(null, points);
    }

    public Route(String name, List<Point> points) {
        this.name = name;
        this.points = new ArrayList<>(Objects.requireNonNull(points, "Points cannot be null"));
    }

    /**
     * Add a point to the route.
     */
    public void addPoint(Point point) {
        this.points.add(Objects.requireNonNull(point, "Point cannot be null"));
    }

    /**
     * Get the number of points in the route.
     */
    public int getPointCount() {
        return points.size();
    }

    /**
     * Get an unmodifiable view of the points.
     */
    public List<Point> getPointsView() {
        return Collections.unmodifiableList(points);
    }
}
