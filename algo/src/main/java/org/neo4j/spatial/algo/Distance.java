package org.neo4j.spatial.algo;

import org.neo4j.spatial.core.LineSegment;
import org.neo4j.spatial.core.Point;
import org.neo4j.spatial.core.Polygon;

public interface Distance {
    /**
     * @param a
     * @param b
     * @return The minimum distance between two polygons. Returns 0 if one polygon is (partially) contained by the other
     */
    double distance(Polygon a, Polygon b);

    /**
     * @param polygon
     * @param lineSegment
     * @return The minimum distance between a polygon and line segment. Returns 0 if the line segment is (partially) contained by the polygon
     */
    double distance(Polygon polygon, LineSegment lineSegment);

    /**
     * @param polygon
     * @param point
     * @return The minimum distance between a polygon and point. Returns 0 if point is within the polygon
     */
    double distance(Polygon polygon, Point point);

    /**
     * @param lineSegment
     * @param point
     * @return The minimum distance between a line segment and a point
     */
    double distance(LineSegment lineSegment, Point point);

    /**
     * @param a
     * @param b
     * @return The minimum distance between two line segments
     */
    double distance(LineSegment a, LineSegment b);

    /**
     * @param p1
     * @param p2
     * @return The minimum distance between two points
     */
    double distance(Point p1, Point p2);
}
