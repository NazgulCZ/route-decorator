# Route Decorator

A tool that decorates GPX routes and tracks with waypoint markers. When you have waypoints that you want to display on a map alongside routes/tracks, this tool finds the nearest point on each route and generates a hexagonal polygon around it.

## Problem Solved

Some map applications cannot display routes and waypoints simultaneously. This tool provides a workaround by embedding waypoint markers directly into the route data as hexagonal polygons.

## Features

- Process GPX routes and tracks from any GPX file
- Parse waypoints from a separate GPX file
- Find the nearest point on the route for each waypoint
- Generate perfect hexagonal markers around waypoint locations
- Configurable marker size (default: 20 meters radius)
- Output a single decorated GPX file ready for display

## Installation & Usage

### Prerequisites

- Java 25 or later ([Download Java](https://www.oracle.com/java/technologies/downloads/))

### Running the Tool

#### Windows Users

1. Download the latest `route-decorator-4.0.jar` file from the releases page
2. Place it in a folder where your GPX files are located (or note the full path to your files)
3. **Option A: Interactive Mode (Recommended for beginners)**
   - Open Command Prompt or PowerShell in the folder with the JAR file
   - Type: `java -jar route-decorator-4.0.jar`
   - The tool will ask you for:
     - Path to your route GPX file
     - Path to your waypoint GPX file
     - Output file name (press Enter for default)
     - Hexagon size in meters (press Enter for default 20 meters)

4. **Option B: Command Line Mode**
   - Open Command Prompt or PowerShell in the folder with the JAR file
   - Example command:
     ```
     java -jar route-decorator-4.0.jar -rf route.gpx -wf waypoints.gpx -of result.gpx
     ```

**Getting the full path to your files:**
- Right-click on a GPX file → Properties → Copy the full path shown as "Location"
- Or use: `cd C:\path\to\your\files` to navigate to the folder first

#### macOS & Linux Users

Download the latest `route-decorator-4.0.jar` file from the releases page and run:

```bash
java -jar route-decorator-4.0.jar
```

The tool will prompt you for the required files, or you can use command-line arguments (see below).

### Command-Line Arguments (All Platforms)

You can provide arguments to avoid interactive prompts:

```bash
java -jar route-decorator-4.0.jar --route-file my-route.gpx --waypoint-file my-waypoints.gpx --output-file decorated-route.gpx --radius 25
```

**Windows Example (in Command Prompt):**
```
java -jar route-decorator-4.0.jar -rf "C:\Users\YourName\Documents\route.gpx" -wf "C:\Users\YourName\Documents\waypoints.gpx" -of "C:\Users\YourName\Documents\result.gpx"
```

**Argument Options:**
- `--route-file` or `-rf` - Path to the GPX file with routes/tracks
- `--waypoint-file` or `-wf` - Path to the GPX file with waypoints
- `--output-file` or `-of` - Path for the output decorated GPX file
- `--radius` - Hexagon radius in meters

### Windows Tips

**To open Command Prompt in a specific folder:**
1. Open the folder in File Explorer
2. Click on the address bar and type `cmd`
3. Press Enter

**To find the full path to a file:**
1. Right-click the file
2. Select Properties
3. Look for "Location" field - this is your folder path
4. Add the filename to the end

**Example:**
If your Location is `C:\Users\John\Documents` and the file is `my-route.gpx`, the full path is:
```
C:\Users\John\Documents\my-route.gpx
```

## How It Works

1. Parses the route GPX file to extract the route/track points
2. Parses the waypoint GPX file to extract waypoint locations
3. For each waypoint, finds the nearest point on the route
4. Generates a perfect hexagon around that point with the specified radius
5. Inserts the hexagon points at the correct position in the route sequence
6. Writes the decorated route to the output GPX file

The resulting GPX file can be opened in any map application that supports GPX files (e.g., Garmin, OSM, etc.).

## Building from Source

If you want to build the project yourself:

### Windows

1. Install Java 25+ and [Gradle 8.0+](https://gradle.org/install/)
2. Open Command Prompt in the project folder
3. Build the fat JAR:
   ```
   gradlew fatJar
   ```
4. The JAR file will be in `cli\build\libs\`

### macOS & Linux

```bash
./gradlew clean build
./gradlew :cli:fatJar
```

To run tests:
```bash
./gradlew test
```

## Requirements

- Java 25+
- Gradle 8.0+ (only for building from source)

## License

MIT
