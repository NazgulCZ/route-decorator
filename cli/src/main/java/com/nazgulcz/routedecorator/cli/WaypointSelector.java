package com.nazgulcz.routedecorator.cli;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Parses a user-provided waypoint selection string like "1,2,5-10" and
 * returns the list of 0-based indices to process. If the selection means
 * "all" waypoints, returns Optional.empty().
 */
public final class WaypointSelector {

    private WaypointSelector() {
        // Utility class
    }

    /**
     * Parse the selection string.
     *
     * @param selection the user selection string, may be null
     * @param waypointCount total number of available waypoints
     * @return Optional.empty() to indicate "all" waypoints, or Optional.of(list)
     *         with the 0-based indices to process (sorted ascending, no duplicates)
     */
    public static Optional<List<Integer>> parseSelection(String selection, int waypointCount) {
        if (selection == null) {
            return Optional.empty();
        }

        String trimmed = selection.trim();
        if (trimmed.isEmpty()) {
            return Optional.empty();
        }

        if (trimmed.equalsIgnoreCase("all")) {
            return Optional.empty();
        }

        String[] tokens = trimmed.split(",");
        Set<Integer> indices = new LinkedHashSet<>(); // preserve insertion order for deterministic warnings
        for (String rawToken : tokens) {
            String token = rawToken.trim();
            if (token.isEmpty()) {
                continue; // silence for empty parts
            }

            if (token.contains("-")) {
                // Range
                String[] parts = token.split("-");
                if (parts.length != 2) {
                    System.err.println("Invalid token ignored: " + token);
                    continue;
                }
                String a = parts[0].trim();
                String b = parts[1].trim();
                try {
                    int start = Integer.parseInt(a);
                    int end = Integer.parseInt(b);
                    if (start > end) {
                        System.err.println("Invalid range (start>end) ignored: " + token);
                        continue;
                    }
                    // convert 1-based to 0-based, validate bounds
                    if (end < 1 || start > waypointCount) {
                        System.err.println("Range out of bounds ignored: " + token);
                        continue;
                    }
                    int s = Math.max(start, 1);
                    int e = Math.min(end, waypointCount);
                    for (int i = s; i <= e; i++) {
                        indices.add(i - 1);
                    }
                } catch (NumberFormatException ex) {
                    System.err.println("Invalid token ignored: " + token);
                }
            } else {
                // Single integer
                try {
                    int idx = Integer.parseInt(token);
                    if (idx < 1 || idx > waypointCount) {
                        System.err.println("Index out of bounds ignored: " + token);
                        continue;
                    }
                    indices.add(idx - 1);
                } catch (NumberFormatException ex) {
                    System.err.println("Invalid token ignored: " + token);
                }
            }
        }

        if (indices.isEmpty()) {
            // Per requirement: empty/invalid selection -> treat as "all"
            return Optional.empty();
        }

        // Return sorted list in ascending order (GPX order)
        List<Integer> result = indices.stream().sorted().collect(Collectors.toList());
        return Optional.of(result);
    }
}
