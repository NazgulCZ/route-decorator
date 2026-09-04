package com.nazgulcz.routedecorator.rest.controller;

import com.nazgulcz.routedecorator.gpx.GpxException;
import com.nazgulcz.routedecorator.gpx.GpxParser;
import com.nazgulcz.routedecorator.gpx.GpxWriter;
import com.nazgulcz.routedecorator.model.Route;
import com.nazgulcz.routedecorator.model.Waypoint;
import com.nazgulcz.routedecorator.cli.WaypointSelector;
import com.nazgulcz.routedecorator.RouteDecorator;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class DecorationController {

    @PostMapping(value = "/decorate", produces = "application/gpx+xml")
    public ResponseEntity<?> decorate(
            @RequestParam("routeFile") MultipartFile routeFile,
            @RequestParam(value = "waypointFile", required = false) MultipartFile waypointFile,
            @RequestParam(value = "radius", required = false) Double radius,
            @RequestParam(value = "waypoints", required = false) String waypointSelection
    ) {
        if (routeFile == null || routeFile.isEmpty()) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Missing required parameter: routeFile"));
        }

        try {
            Route route = GpxParser.parseRoute(routeFile.getInputStream());
            if (route == null || route.getPointCount() == 0) {
                return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                        .body(Map.of("error", "No route or track found in provided GPX file"));
            }

            List<Waypoint> waypoints = new ArrayList<>();
            if (waypointFile != null && !waypointFile.isEmpty()) {
                waypoints = GpxParser.parseWaypoints(waypointFile.getInputStream());
            }

            // Apply waypoint selection if provided
            if (waypointSelection != null && !waypointSelection.trim().isEmpty() && !waypoints.isEmpty()) {
                List<Integer> indices = WaypointSelector.parseSelection(waypointSelection, waypoints.size());
                if (!indices.isEmpty()) {
                    waypoints = indices.stream().map(waypoints::get).collect(Collectors.toList());
                }
                // empty indices means "all" per WaypointSelector
            }

            RouteDecorator decorator = (radius == null) ? new RouteDecorator() : new RouteDecorator(radius);
            Route decorated = decorator.decorate(route, waypoints);

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            GpxWriter.write(decorated, bout);
            byte[] bytes = bout.toByteArray();

            ByteArrayResource resource = new ByteArrayResource(bytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=decorated.gpx")
                    .contentLength(bytes.length)
                    .contentType(MediaType.parseMediaType("application/gpx+xml"))
                    .body(resource);

        } catch (GpxException e) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Failed to parse GPX: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Server error: " + e.getMessage()));
        }
    }
}
