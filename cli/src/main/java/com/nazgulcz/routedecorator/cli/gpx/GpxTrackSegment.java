package com.nazgulcz.routedecorator.cli.gpx;

import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * JAXB model for a GPX track segment (trkseg).
 */
@Getter
@Setter
@NoArgsConstructor
public class GpxTrackSegment {
    @XmlElement(name = "trkpt")
    private List<GpxPoint> points;
}
