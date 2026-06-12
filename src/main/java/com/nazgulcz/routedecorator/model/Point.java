package com.nazgulcz.routedecorator.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a geographic point with latitude and longitude coordinates.
 */
@Getter
@ToString
@EqualsAndHashCode
public class Point {
    private final double latitude;
    private final double longitude;
    private final double elevation;

    public Point(double latitude, double longitude) {
        this(latitude, longitude, 0.0);
    }

    public Point(double latitude, double longitude, double elevation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    /**
     * Calculate Euclidean distance to another point.
     * Note: This uses simple Euclidean distance, not geodetic distance.
     */
    public double distanceTo(Point other) {
        double dLat = this.latitude - other.latitude;
        double dLon = this.longitude - other.longitude;
        return Math.sqrt(dLat * dLat + dLon * dLon);
    }
}
