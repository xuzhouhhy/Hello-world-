/**
 * Immutable Point3D, using the concept of 'Value Object' from
 * 'Domain-Driven Design'
 */
package com.xuzhouhhy.baidumap.data;


import com.xuzhouhhy.baidumap.util.UtilMath;

/**
 * @author Jeffrey
 */
public final class Point3D {
    private final double mX;
    private final double mY;
    private final double mZ;

    public static Point3D getInvalidPoint() {
        return PointHolder.INVALID_POINT;
    }

    public static Point3D getOriginalPoint() {
        return PointHolder.ORIGIN_POINT;
    }

    public Point3D(double x, double y, double z) {
        mX = x;
        mY = y;
        mZ = z;
    }

    public Point3D() {
        this(0, 0, 0);
    }

    public Point3D(Point3D other) {
        this(other.getX(), other.getY(), other.getZ());
    }

    public Point3D(Point2D other) {
        this(other.getX(), other.getY(), 0);
    }

    public Point3D(double[] xyz) {
        this(xyz[0], xyz[1], xyz[2]);
    }

    public Point3D(Point3DMutable pm) {
        this(pm.getX(), pm.getY(), pm.getZ());
    }

    public boolean isValid() {
        return (!Double.isNaN(mX) && !Double.isNaN(mY) && !Double.isNaN(mZ));
    }

    public double getX() {
        return mX;
    }

    public double getY() {
        return mY;
    }

    public double getZ() {
        return mZ;
    }

    public double[] getValues() {
        return new double[]{mX, mY, mZ};
    }

    public boolean equals(Point3D other, double tolerance) {
        return (Math.abs(mX - other.getX()) <= tolerance)
                && (Math.abs(mY - other.getY()) <= tolerance)
                && (Math.abs(mZ - other.getZ()) <= tolerance);
    }

    public boolean equals(Point3D other) {
        return equals(other, 1e-5);
    }

    public double getLength2D() {
        return UtilMath.rss(mX, mY);
    }

    public double getLength3D() {
        return UtilMath.rss(mX, mY, mZ);
    }

    public double lengthTo2D(Point3D other) {
        return other.minus(this).getLength2D();
    }

    public double lengthTo3D(Point3D other) {
        return other.minus(this).getLength3D();
    }

    public Point3D minus(Point3D other) {
        return minus(other.getX(), other.getY(), other.getZ());
    }

    public Point3D add(Point3D other) {
        return add(other.getX(), other.getY(), other.getZ());
    }

    public Point3D add(double dx, double dy, double dz) {
        return new Point3D(mX + dx, mY + dy, mZ + dz);
    }

    public Point3D minus(double dx, double dy, double dz) {
        return add(-dx, -dy, -dz);
    }

    public static Point3D minus(Point3D left, Point3D right) {
        return left.minus(right);
    }

    public static Point3D add(Point3D left, Point3D right) {
        return left.add(right);
    }

    public static double calcLength2D(Point3D p1, Point3D p2) {
        return p1.lengthTo2D(p2);
    }

    public static double calcLength3D(Point3D p1, Point3D p2) {
        return p1.lengthTo3D(p2);
    }

    public double dotProduct2D(Point3D p2) {
        return mX * p2.getX() + mY * p2.getY();
    }

    public double crossProduct2D(Point3DMutable p2) {
        return mX * p2.getY() - mY * p2.getX();
    }

    public Point2D toPoint2D() {
        return new Point2D(getX(), getY());
    }

    public Point3DMutable toMutable() {
        return new Point3DMutable(getX(), getY(), getZ());
    }

    @Override
    public String toString() {
        return "[" + mX + "," + mY + "," + mZ + "]";
    }

    private static final class PointHolder {
        private static final Point3D INVALID_POINT = new Point3D(Double.NaN, Double.NaN, Double.NaN);
        private static final Point3D ORIGIN_POINT = new Point3D(0.0, 0.0, 0.0);
    }
}
