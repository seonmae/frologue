package com.banuh.frologue.core.utils;

public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this(0, 0);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public Vector2D add(double dx, double dy) {
        return new Vector2D(this.x + dx, this.y + dy);
    }

    public Vector2D addX(double dx) {
        return new Vector2D(this.x + dx, this.y);
    }

    public Vector2D addY(double dy) {
        return new Vector2D(this.x, this.y + dy);
    }

    public Vector2D multiply(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    public Vector2D divide(double scalar) {
        if (scalar == 0) throw new ArithmeticException("Division by zero");
        return new Vector2D(this.x / scalar, this.y / scalar);
    }

    public double dot(Vector2D other) {
        return this.x * other.x + this.y * other.y;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D normalize() {
        double mag = magnitude();
        if (mag == 0) return new Vector2D(0, 0);
        return this.divide(mag);
    }

    public double distance(Vector2D other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    public void added(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void subtracted(Vector2D other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    public void added(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void addedX(double dx) {
        this.x += dx;
    }

    public void addedY(double dy) {
        this.y += dy;
    }

    public void multiplied(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    public void divided(double scalar) {
        if (scalar == 0) throw new ArithmeticException("Division by zero");
        this.x /= scalar;
        this.y /= scalar;
    }

    public void normalized() {
        double mag = Math.sqrt(x * x + y * y);
        if (mag == 0) return;
        this.x /= mag;
        this.y /= mag;
    }

    public void set(Vector2D other) {
        this.x = other.x;
        this.y = other.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
