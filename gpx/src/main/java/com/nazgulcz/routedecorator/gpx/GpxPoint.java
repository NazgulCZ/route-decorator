package com.nazgulcz.routedecorator.gpx;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JAXB model for a GPX trkpt or rtept (track/route point).
 */
@Getter
@Setter
@NoArgsConstructor
public class GpxPoint {
    @XmlAttribute
    private double lat;

    @XmlAttribute
    private double lon;

    @XmlElement
    private Double ele;

    @XmlElement
    private String name;
}
