package org.neo4j.spatial.core;

import java.util.Arrays;
import java.util.StringJoiner;

public class Vector {
    private double[] coordinates;

    public Vector(double... coordinates) {
        this.coordinates = coordinates;
    }

    public double getCoordinate(int i) {
        return coordinates[i];
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public Vector add(Vector shifts) {
        double[] shifted = Arrays.copyOf(this.getCoordinates(), this.getCoordinates().length);
        for (int i = 0; i < shifted.length; i++) {
            shifted[i] += shifts.getCoordinates()[i];
        }
        return new Vector(shifted);
    }

    public Vector subtract(Vector shifts) {
        double[] shifted = Arrays.copyOf(this.getCoordinates(), this.getCoordinates().length);
        for (int i = 0; i < shifted.length; i++) {
            shifted[i] -= shifts.getCoordinates()[i];
        }
        return new Vector(shifted);
    }

    public Vector multiply(double scalar) {
        double[] shifted = Arrays.copyOf(this.getCoordinates(), this.getCoordinates().length);
        for (int i = 0; i < shifted.length; i++) {
            shifted[i] *= scalar;
        }
        return new Vector(shifted);
    }

    public Vector divide(double scalar) {
        double[] shifted = Arrays.copyOf(this.getCoordinates(), this.getCoordinates().length);
        for (int i = 0; i < shifted.length; i++) {
            shifted[i] /= scalar;
        }
        return new Vector(shifted);
    }

    public double magnitude() {
        return Math.sqrt(coordinates[0] * coordinates[0] + coordinates[1] * coordinates[1] + coordinates[2] * coordinates[2]);
    }

    public double dot(Vector other) {
        if (this.getCoordinates().length != other.getCoordinates().length) {
            throw new IllegalArgumentException("Vectors do not have the same dimension");
        }

        double sum = 0;
        for (int i = 0; i < this.getCoordinates().length; i++) {
            sum += this.getCoordinates()[i] * other.getCoordinates()[i];
        }
        return sum;
    }

    public Vector cross(Vector other) {
        return new Vector(
                this.getCoordinate(1) * other.getCoordinate(2) - this.getCoordinate(2) * other.getCoordinate(1),
                this.getCoordinate(2) * other.getCoordinate(0) - this.getCoordinate(0) * other.getCoordinate(2),
                this.getCoordinate(0) * other.getCoordinate(1) - this.getCoordinate(1) * other.getCoordinate(0)
        );
    }

    public boolean equals(Vector other) {
        return Arrays.equals(this.coordinates, other.getCoordinates());
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Vector && this.equals((Vector) other);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "Vector{", "}");
        for (double coordinate : coordinates) {
            joiner.add(coordinate + "");
        }
        return joiner.toString();
    }
}