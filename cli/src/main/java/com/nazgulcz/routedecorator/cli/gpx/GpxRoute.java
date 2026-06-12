package com.nazgulcz.routedecorator.cli.gpx;

import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * JAXB model for a GPX route (rte).
 */
@Getter
@Setter
@NoArgsConstructor
public class GpxRoute {
    @XmlElement
    private String name;

    @XmlElement(name = "rtept")
    private List<GpxPoint> points;
}
