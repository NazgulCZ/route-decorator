package com.nazgulcz.routedecorator.cli;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Parses a user-provided waypoint selection string like "1,2,5-10" and
 * returns the list of 0-based indices to process. An empty list indicates "all".
 */
public final class WaypointSelector {

    private WaypointSelector() {}

    public static List<Integer> parseSelection(String selection, int waypointCount) {
        if (selection == null) {
            return Collections.emptyList();
        }

        String trimmed = selection.trim();
        if (trimmed.isEmpty() || trimmed.equalsIgnoreCase("all")) {
            return Collections.emptyList();
        }

        String[] tokens = trimmed.split(",");
        Set<Integer> indices = new LinkedHashSet<>();
        for (String rawToken : tokens) {
            String token = rawToken.trim();
            if (token.isEmpty()) {
                continue;
            }

            if (token.contains("-")) {
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
            return Collections.emptyList();
        }

        return indices.stream().sorted().collect(Collectors.toList());
    }
}
