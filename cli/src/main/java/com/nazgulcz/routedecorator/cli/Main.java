package com.nazgulcz.routedecorator.cli;

import com.nazgulcz.routedecorator.RouteDecorator;
import com.nazgulcz.routedecorator.model.Route;
import com.nazgulcz.routedecorator.model.Waypoint;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final String DEFAULT_OUTPUT_FILE = "output.gpx";
    private static final double DEFAULT_RADIUS = -1.0; // Indicates use RouteDecorator's default

    public static void main(String[] args) {
        try {
            CliArgs cliArgs = CliArgs.parse(args);

            Scanner scanner = new Scanner(System.in);
            
            Path routeFile = getRoutePath(cliArgs, scanner);
            Path waypointFile = getWaypointPath(cliArgs, scanner);
            Path outputFile = getOutputPath(cliArgs);
            double radius = cliArgs.getRadius();

            // Validate inputs
            validateFile(routeFile, "Route file");
            validateFile(waypointFile, "Waypoint file");
            validateOutputFile(outputFile);
            if (radius != DEFAULT_RADIUS) {
                validateRadius(radius);
            }

            // Parse GPX files
            Route route = GpxParser.parseRoute(routeFile);
            List<Waypoint> waypoints = GpxParser.parseWaypoints(waypointFile);

            if (route == null || route.getPointCount() == 0) {
                error("Route file does not contain any valid route or track");
            }

            if (waypoints.isEmpty()) {
                error("Waypoint file does not contain any valid waypoints");
            }

            List<Waypoint> toProcess = selectWaypoints(waypoints, cliArgs, scanner);

            // Decorate route
            RouteDecorator decorator = radius == DEFAULT_RADIUS 
                ? new RouteDecorator() 
                : new RouteDecorator(radius);
            Route decoratedRoute = decorator.decorate(route, toProcess);

            // Write output
            GpxWriter.write(decoratedRoute, outputFile);
            success("Route decorated successfully! Output written to: " + outputFile);

        } catch (CliException e) {
            error(e.getMessage());
        } catch (Exception e) {
            error("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<Waypoint> selectWaypoints(List<Waypoint> waypoints, CliArgs cliArgs, Scanner scanner) {
        String selection = cliArgs.getWaypointSelection();
        if (selection == null) {
            System.out.println("Waypoints:");
            for (int i = 0; i < waypoints.size(); i++) {
                Waypoint wp = waypoints.get(i);
                System.out.println((i + 1) + ". " + wp.getName());
            }

            System.out.print("Enter waypoints to process (e.g. 1,2,5-10) or press Enter for all: ");
            selection = scanner.nextLine();
        } else {
            if (selection.equalsIgnoreCase("all")) {
                selection = "";
            }
            // when passed via CLI, do not print the list or prompt
        }

        List<Integer> indices = WaypointSelector.parseSelection(selection, waypoints.size());
        if (indices.isEmpty()) {
            return waypoints; // treat as all
        }

        List<Waypoint> toProcess = new ArrayList<>();
        for (int idx : indices) {
            toProcess.add(waypoints.get(idx));
        }
        return toProcess;
    }

    private static Path getRoutePath(CliArgs cliArgs, Scanner scanner) throws CliException {
        Path routeFile = cliArgs.getRouteFile();
        if (routeFile != null) {
            return routeFile;
        }

        System.out.print("Enter route file path (required): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new CliException("Route file path cannot be empty");
        }
        return Paths.get(input);
    }

    private static Path getWaypointPath(CliArgs cliArgs, Scanner scanner) throws CliException {
        Path waypointFile = cliArgs.getWaypointFile();
        if (waypointFile != null) {
            return waypointFile;
        }

        System.out.print("Enter waypoint file path (required): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new CliException("Waypoint file path cannot be empty");
        }
        return Paths.get(input);
    }

    private static Path getOutputPath(CliArgs cliArgs) {
        Path outputFile = cliArgs.getOutputFile();
        return outputFile != null ? outputFile : Paths.get(DEFAULT_OUTPUT_FILE);
    }

    private static void validateFile(Path file, String fileType) throws CliException {
        if (!Files.exists(file)) {
            throw new CliException(fileType + " does not exist: " + file);
        }
        if (!Files.isReadable(file)) {
            throw new CliException(fileType + " is not readable: " + file);
        }
    }

    private static void validateOutputFile(Path file) throws CliException {
        if (Files.exists(file) && !Files.isWritable(file)) {
            throw new CliException("Output file is not writable: " + file);
        }
        Path parent = file.getParent();
        if (parent != null && !Files.isWritable(parent)) {
            throw new CliException("Output directory is not writable: " + parent);
        }
    }

    private static void validateRadius(double radius) throws CliException {
        if (radius <= 0) {
            throw new CliException("Radius must be greater than 0");
        }
    }

    private static void error(String message) {
        System.err.println("❌ Error: " + message);
        System.exit(1);
    }

    private static void success(String message) {
        System.out.println("✓ " + message);
    }
}
