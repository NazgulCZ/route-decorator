package com.nazgulcz.routedecorator.cli.gpx;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * JAXB model for the root GPX document.
 */
@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "gpx")
@XmlAccessorType(XmlAccessType.FIELD)
public class GpxDocument {
    @XmlElement(name = "wpt")
    private List<GpxWaypoint> waypoints;

    @XmlElement(name = "rte")
    private List<GpxRoute> routes;

    @XmlElement(name = "trk")
    private List<GpxTrack> tracks;
}
