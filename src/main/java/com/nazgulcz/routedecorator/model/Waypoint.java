package com.nazgulcz.routedecorator.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * Represents a waypoint with a name and geographic location.
 */
@Getter
@ToString
@EqualsAndHashCode
public class Waypoint {
    private final String name;
    private final Point point;

    public Waypoint(String name, Point point) {
        this.name = Objects.requireNonNull(name, "Waypoint name cannot be null");
        this.point = Objects.requireNonNull(point, "Waypoint point cannot be null");
    }
}
