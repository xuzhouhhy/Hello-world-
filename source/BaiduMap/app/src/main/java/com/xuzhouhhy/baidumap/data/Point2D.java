/**
 * Immutable Point2D, using the concept of 'Value Object' from
 * 'Domain-Driven Design'
 */
package com.xuzhouhhy.baidumap.data;


import com.xuzhouhhy.baidumap.util.UtilMath;

/**
 * @author Jeffrey
 */
public final class Point2D {
    private final double mX;
    private final double mY;

    public static Point2D getInvalidPoint() {
        return PointHolder.INVALID_POINT;
    }

    public Point2D(double x, double y) {
        mX = x;
        mY = y;
    }

    public Point2D() {
        this(0, 0);
    }

    public Point2D(Point2D other) {
        this(other.getX(), other.getY());
    }

    public Point2D(Point3D other) {
        this(other.getX(), other.getY());
    }

    public Point2D(Point2DMutable p2d) {
        this(p2d.getX(), p2d.getY());
    }

    public double getX() {
        return mX;
    }

    public double getY() {
        return mY;
    }

    public boolean equals(Point2D other, double tolerance) {
        return (Math.abs(mX - other.getX()) <= tolerance) &&
                (Math.abs(mY - other.getY()) <= tolerance);
    }

    public boolean equals(Point2D other) {
        return equals(other, 1e-5);
    }

    public double getLength() {
        return UtilMath.rss(mX, mY);
    }

    public double lengthTo(Point2D p) {
        return p.minus(this).getLength();
    }

    public Point2D minus(Point2D other) {
        return minus(other.getX(), other.getY());
    }

    public Point2D minus(double x, double y) {
        return add(-x, -y);
    }

    public Point2D add(Point2D other) {
        return add(other.getX(), other.getY());
    }

    public Point2D add(double x, double y) {
        return new Point2D(mX + x, mY + y);
    }

    public static Point2D minus(Point2D left, Point2D right) {
        return left.minus(right);
    }

    public static Point2D add(Point2D left, Point2D right) {
        return left.add(right);
    }

    public double dotProduct(Point2D p2) {
        return mX * p2.getX() + mY * p2.getY();
    }

    public double crossProduct(Point2D p2) {
        return mX * p2.getY() - mY * p2.getX();
    }

    public static double calcLength(Point2D p1, Point2D p2) {
        return p1.lengthTo(p2);
    }

    public Point3D toPoint3D() {
        return new Point3D(mX, mY, 0.0);
    }

    public Point2DMutable toMutable() {
        return new Point2DMutable(mX, mY);
    }

    public boolean isValid() {
        return (!Double.isNaN(mX) && !Double.isNaN(mY));
    }

    @Override
    public String toString() {
        return "[" + mX + "," + mY + "]";
    }

    private static final class PointHolder {
        private static final Point2D INVALID_POINT = new Point2D(Double.NaN, Double.NaN);
    }
}
