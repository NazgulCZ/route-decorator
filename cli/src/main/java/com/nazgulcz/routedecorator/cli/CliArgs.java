package com.nazgulcz.routedecorator.cli;

import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Parses and holds command-line arguments.
 */
@Getter
public class CliArgs {
    private static final double DEFAULT_RADIUS = -1.0; // Indicates use RouteDecorator's default

    private Path routeFile;
    private Path waypointFile;
    private Path outputFile;
    private double radius;

    private CliArgs() {
        this.radius = DEFAULT_RADIUS;
    }

    /**
     * Parse command-line arguments.
     * Supported: --route-file, -rf, --waypoint-file, -wf, --output-file, -of, --radius
     */
    public static CliArgs parse(String[] args) throws CliException {
        CliArgs cliArgs = new CliArgs();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equals("--route-file") || arg.equals("-rf")) {
                cliArgs.routeFile = parsePathArg(args, i, "--route-file");
                i++;
            } else if (arg.equals("--waypoint-file") || arg.equals("-wf")) {
                cliArgs.waypointFile = parsePathArg(args, i, "--waypoint-file");
                i++;
            } else if (arg.equals("--output-file") || arg.equals("-of")) {
                cliArgs.outputFile = parsePathArg(args, i, "--output-file");
                i++;
            } else if (arg.equals("--radius")) {
                cliArgs.radius = parseRadiusArg(args, i);
                i++;
            } else {
                throw new CliException("Unknown argument: " + arg);
            }
        }

        return cliArgs;
    }

    private static Path parsePathArg(String[] args, int index, String argName) throws CliException {
        if (index + 1 >= args.length) {
            throw new CliException(argName + " requires a value");
        }
        return Paths.get(args[index + 1]);
    }

    private static double parseRadiusArg(String[] args, int index) throws CliException {
        if (index + 1 >= args.length) {
            throw new CliException("--radius requires a value");
        }
        try {
            return Double.parseDouble(args[index + 1]);
        } catch (NumberFormatException e) {
            throw new CliException("--radius must be a valid number: " + args[index + 1]);
        }
    }
}
