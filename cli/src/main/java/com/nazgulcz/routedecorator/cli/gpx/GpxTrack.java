package com.nazgulcz.routedecorator.cli.gpx;

import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * JAXB model for a GPX track (trk).
 */
@Getter
@Setter
@NoArgsConstructor
public class GpxTrack {
    @XmlElement
    private String name;

    @XmlElement(name = "trkseg")
    private List<GpxTrackSegment> segments;
}
