# Route Decorator

A Java library that decorates GPX routes and tracks with waypoint markers. When you have waypoints that you want to display on a map alongside routes/tracks, this library finds the nearest point on the route for each waypoint and draws a small polygon (hexagon) around it, creating a visual marker that can be displayed together with the route.

## Problem Solved

Some map applications cannot display routes and waypoints simultaneously. This library provides a workaround by embedding waypoint markers directly into the route data as small polygons.

## Features

- Process GPX routes and tracks
- Find nearest points on routes for given waypoints
- Generate hexagonal polygons around waypoint locations
- Configurable polygon size (default: 10 meters)
- Return decorated routes ready for display

## Building

```bash
./gradlew build
```

## Testing

```bash
./gradlew test
```

## Requirements

- Java 25+
- Gradle 8.0+

## License

MIT
