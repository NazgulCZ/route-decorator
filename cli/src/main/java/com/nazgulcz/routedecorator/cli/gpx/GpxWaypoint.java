package com.nazgulcz.routedecorator.cli.gpx;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JAXB model for a GPX waypoint (wpt).
 */
@Getter
@Setter
@NoArgsConstructor
public class GpxWaypoint {
    @XmlAttribute
    private double lat;

    @XmlAttribute
    private double lon;

    @XmlElement
    private Double ele;

    @XmlElement
    private String name;
}
