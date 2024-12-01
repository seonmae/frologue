package com.banuh.frologue.core.utils;

public class Vector2D {
    public enum Direction {
        LEFT, TOP, RIGHT, BOTTOM;
    }

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

    public Vector2D added(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2D subtracted(Vector2D other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector2D added(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        return this;
    }

    public Vector2D addedX(double dx) {
        this.x += dx;
        return this;
    }

    public Vector2D addedY(double dy) {
        this.y += dy;
        return this;
    }

    public Vector2D multiplied(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public Vector2D divided(double scalar) {
        if (scalar == 0) throw new ArithmeticException("Division by zero");
        this.x /= scalar;
        this.y /= scalar;
        return this;
    }

    public Vector2D normalized() {
        double mag = Math.sqrt(x * x + y * y);
        if (mag == 0) return null;
        this.x /= mag;
        this.y /= mag;
        return this;
    }

    public void set(Vector2D other) {
        this.x = other.x;
        this.y = other.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void reset() {
        this.x = 0;
        this.y = 0;
    }

    public void flipX() {
        this.x = -x;
    }

    public void flipY() {
        this.y = -y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static Direction getOverlapDirection(Vector2D overlap) {
        if (overlap == null) return null;

        // 세로가 더 길다 = 옆으로 충돌했다
        if (Math.abs(overlap.getX()) >= Math.abs(overlap.getY())) {
            // x가 양수다 = 왼쪽으로 충돌했다
            return overlap.getX() > 0 ? Direction.LEFT : Direction.RIGHT;
        } else {
            // x가 양수다 = 아래쪽으로 충돌했다
            return overlap.getY() > 0 ? Direction.TOP : Direction.BOTTOM;
        }
    }

    public static boolean isCollision(
        double ax1, double ay1, double ax2, double ay2,
        double bx1, double by1, double bx2, double by2
    ) {
        return ax1 < bx2 && ax2 > bx1 && ay1 < by2 && ay2 > by1;
    }
}
