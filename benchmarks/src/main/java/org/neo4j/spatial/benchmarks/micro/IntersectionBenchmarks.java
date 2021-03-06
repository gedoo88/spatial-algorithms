package org.neo4j.spatial.benchmarks.micro;

import org.neo4j.internal.helpers.collection.Pair;
import org.neo4j.spatial.algo.Intersect;
import org.neo4j.spatial.algo.IntersectCalculator;
import org.neo4j.spatial.benchmarks.JfrProfiler;
import org.neo4j.spatial.core.CRS;
import org.neo4j.spatial.core.LineSegment;
import org.neo4j.spatial.core.Point;
import org.neo4j.spatial.core.Polygon;
import org.neo4j.spatial.viewer.Viewer;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

@State(Scope.Benchmark)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
public class IntersectionBenchmarks {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(IntersectionBenchmarks.class.getSimpleName())
                .forks(1)
                .addProfiler(JfrProfiler.class)
                .build();

        new Runner(opt).run();
    }

    private Map<String, Polygon.SimplePolygon[]> geographicData = new LinkedHashMap<>();
    private Map<String, Polygon.SimplePolygon[]> cartesianData = new LinkedHashMap<>();
    private Intersect geographicNaiveCalculator = IntersectCalculator.getCalculator(CRS.WGS84, IntersectCalculator.AlgorithmVariant.Naive);
    private Intersect geographicSweepCalculator = IntersectCalculator.getCalculator(CRS.WGS84, IntersectCalculator.AlgorithmVariant.MCSweepLine);
    private Intersect cartesianNaiveCalculator = IntersectCalculator.getCalculator(CRS.Cartesian, IntersectCalculator.AlgorithmVariant.Naive);
    private Intersect cartesianSweepCalculator = IntersectCalculator.getCalculator(CRS.Cartesian, IntersectCalculator.AlgorithmVariant.MCSweepLine);

    @Setup
    public void setup() {
        int n = 100;

        Map<String, Point> regions = new LinkedHashMap<>();
        regions.put("US", Point.point(CRS.WGS84, -122.31, 37.56));    // San Francisco
        regions.put("EU", Point.point(CRS.WGS84, 12.99, 55.61));      // Malmo (Neo4j)
        regions.put("OZ", Point.point(CRS.WGS84, 151.17, -33.90));    // Sydney

        Random random = new Random(0);
        for (String region : regions.keySet()) {
            Point origin = regions.get(region);
            Polygon.SimplePolygon[] geographicPolygons = new Polygon.SimplePolygon[n];
            Polygon.SimplePolygon[] cartesianPolygons = new Polygon.SimplePolygon[n];
            for (int i = 0; i < n; i++) {
                Pair<Polygon.SimplePolygon, Polygon.SimplePolygon> polygonPair = MicroBenchmarkUtil.createPolygon(random, origin, 0.1, 5.0, 0.95, 1.05);
                geographicPolygons[i] = polygonPair.first();
                cartesianPolygons[i] = polygonPair.other();
            }
            geographicData.put(region, geographicPolygons);
            cartesianData.put(region, cartesianPolygons);
        }
    }

//    @Benchmark
//    public void testCartesianIntersectLinesegments(Blackhole bh) {
//        for (double x1 = -1000.0; x1 < 1000.0; x1 += 1.0) {
//            for (double x2 = -1000.0; x2 < 1000.0; x2 += 1.0) {
//                LineSegment a = LineSegment.lineSegment(Point.point(CRS.Cartesian, x1, 10), Point.point(CRS.Cartesian, -x1, -10));
//                LineSegment b = LineSegment.lineSegment(Point.point(CRS.Cartesian, x2, 10), Point.point(CRS.Cartesian, -x2, -10));
//
//                bh.consume(cartesianNaiveCalculator.intersect(a, b));
//            }
//        }
//    }
//
//    @Benchmark
//    public void testGeographicIntersectLinesegments(Blackhole bh) {
//        for (double x1 = -10.0; x1 < 10.0; x1 += 0.01) {
//            for (double x2 = -10.0; x2 < 10.0; x2 += 0.01) {
//                LineSegment a = LineSegment.lineSegment(Point.point(CRS.WGS84, x1, 10), Point.point(CRS.WGS84, -x1, -10));
//                LineSegment b = LineSegment.lineSegment(Point.point(CRS.WGS84, x2, 10), Point.point(CRS.WGS84, -x2, -10));
//
//                bh.consume(geographicNaiveCalculator.intersect(a, b));
//            }
//        }
//    }

//    @Benchmark
//    public void testCartesianIntersectPolygonsNaive(Blackhole bh) {
//        for (String region : cartesianData.keySet()) {
//            Polygon.SimplePolygon[] polygons = cartesianData.get(region);
//            for (int i = 0; i < polygons.length; i++) {
//                for (int j = i + 1; j < polygons.length; j++) {
//                    bh.consume(cartesianNaiveCalculator.intersect(polygons[i], polygons[j]));
//                }
//            }
//        }
//    }

//    @Benchmark
//    public void testCartesianIntersectPolygonsSweep(Blackhole bh) {
//        for (String region : cartesianData.keySet()) {
//            Polygon.SimplePolygon[] polygons = cartesianData.get(region);
//            for (int i = 0; i < polygons.length; i++) {
//                for (int j = i + 1; j < polygons.length; j++) {
//                    bh.consume(cartesianSweepCalculator.intersect(polygons[i], polygons[j]));
//                }
//            }
//        }
//    }

//    @Benchmark
//    public void testGeographicIntersectPolygonsNaive(Blackhole bh) {
//        for (String region : geographicData.keySet()) {
//            Polygon.SimplePolygon[] polygons = geographicData.get(region);
//            for (int i = 0; i < polygons.length; i++) {
//                for (int j = i + 1; j < polygons.length; j++) {
//                    bh.consume(geographicNaiveCalculator.intersect(polygons[i], polygons[j]));
//                }
//            }
//        }
//    }

    @Benchmark
    public void testGeographicIntersectPolygonsSweep(Blackhole bh) {
        for (String region : geographicData.keySet()) {
            Polygon.SimplePolygon[] polygons = geographicData.get(region);
            for (int i = 0; i < polygons.length; i++) {
                for (int j = i + 1; j < polygons.length; j++) {
                    bh.consume(geographicSweepCalculator.intersect(polygons[i], polygons[j]));
                }
            }
        }
    }

    public void testCartesianIntersectPolygonsNaiveX() {
        System.out.println("Testing regions: " + cartesianData.keySet());
        for (String region : cartesianData.keySet()) {
            Polygon.SimplePolygon[] polygons = cartesianData.get(region);
            System.out.println("\tTesting region: " + region + " of " + polygons.length + " polygons");
            for (int i = 0; i < polygons.length; i++) {
                System.out.println(polygons[i].toWKT());
                for (int j = i + 1; j < polygons.length; j++) {
                    Point[] points = cartesianNaiveCalculator.intersect(polygons[i], polygons[j]);
                    System.out.println("\t\t" + region + ": intersection(polygon_" + i + "[" + polygons[i].getPoints().length + "], polygon_" + j + "[" + polygons[j].getPoints().length + "]) = points[" + points.length + "]");
                }
            }
        }
    }

    public void testCartesianIntersectPolygonsNaiveWKT() {
        System.out.println("Testing regions: " + cartesianData.keySet());
        for (String region : cartesianData.keySet()) {
            Viewer viewer = new Viewer();
            Polygon.SimplePolygon[] polygons = cartesianData.get(region);
            System.out.println("\tTesting region: " + region + " of " + polygons.length + " polygons");
            System.out.println(polygons[0].toWKT());
            System.out.println(polygons[1].toWKT());
            for (int i = 0; i < 10; i++) {
                viewer.addPolygon(polygons[i].toWKT(), i);
            }
            Point[] points = cartesianNaiveCalculator.intersect(polygons[0], polygons[1]);
            System.out.println("\t\t" + region + ": intersection(polygon_" + 0 + "[" + polygons[0].getPoints().length + "], polygon_" + 1 + "[" + polygons[1].getPoints().length + "]) = points[" + points.length + "]");
            viewer.view();
        }
    }
}
