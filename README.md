# route-decorator

A tiny command-line tool that decorates GPX routes by drawing small polygon markers (regular hexagons) around waypoint locations so they remain visible in map viewers.

Important requirement
- Java 25 (JDK 25) is required to run the prebuilt JAR.

Download the prebuilt JAR
- Get the runnable JAR named like `route-decorator-<version>.jar` from the project's Releases page on GitHub.

Quick usage
- Run the tool with Java 25:

  java -jar route-decorator-<version>.jar \
    --route-file path/to/route.gpx \
    --waypoint-file path/to/waypoints.gpx \
    --output-file path/to/decorated.gpx \
    [--radius <meters>]

Required options
- `--route-file`, `-rf`   Path to the GPX file containing the route or track to decorate
- `--waypoint-file`, `-wf` Path to the GPX file containing waypoints
- `--output-file`, `-of`  Path to write the decorated GPX output

Optional
- `--radius`            Radius of the hexagon in meters (default: 20.0)
- `--waypoints`, `-w`   Optional waypoint selection string (if supported)

What the tool does
- For each waypoint the tool:
  1. Finds the nearest point on the route polyline.
  2. Generates a small hexagon centered on that route point.
  3. Inserts an 8‑point branch into the route: a short connector from the route to the hexagon, the 6 hex vertices, and a connector back to the route center.
- The resulting GPX can be opened in any GPX viewer or mapping application that supports routes/tracks.

Troubleshooting
- "Unsupported major.minor version" or similar: install JDK 25 and run with the `java` from that JDK.
- "File not found": check file paths and permissions.
- Hexagons not visible in your map viewer: some viewers collapse very short branches or hide dense point clusters — try increasing `--radius` to make the polygon larger, or open the output GPX in a text editor to confirm the inserted points are present.

Help / issues
- If you encounter a problem with the prebuilt JAR or the decorated GPX output, open an issue on the project repository: https://github.com/NazgulCZ/route-decorator/issues
