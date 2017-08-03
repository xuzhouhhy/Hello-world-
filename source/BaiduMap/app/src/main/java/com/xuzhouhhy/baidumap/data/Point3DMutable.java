package com.xuzhouhhy.baidumap.data;


import com.xuzhouhhy.baidumap.util.UtilMath;

import java.io.Serializable;

/**
 * @author Lucifer
 * @brief
 * @2015-3-19
 */
public class Point3DMutable implements Serializable {
    /**
     * 默认的序列化ID 防止类升级时反序列化出现异常
     */
    private static final long serialVersionUID = 1L;
    private double x;
    private double y;
    private double z;

    public Point3DMutable(double x, double y, double z) {
        super();
        setValue(x, y, z);
    }

    public Point3DMutable() {
        this(0, 0, 0);
    }

    public Point3DMutable(Point3DMutable other) {
        this(other.x, other.y, other.z);
    }

    public Point3DMutable(Point2DMutable other) {
        this(other.getX(), other.getY(), 0);
    }

    public Point3DMutable(double[] xyz) {
        this(xyz[0], xyz[1], xyz[2]);
    }

    public void setValue(Point3DMutable other) {
        setValue(other.x, other.y, other.z);
    }

    public void setValue(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setValue(double[] xyz) {
        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
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

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double[] getValues() {
        return new double[]{x, y, z};
    }

    public boolean equals(Point3DMutable other, double tolerance) {
        return (Math.abs(x - other.x) <= tolerance)
                && (Math.abs(y - other.y) <= tolerance)
                && (Math.abs(z - other.z) <= tolerance);
    }

    public boolean equals(Point3DMutable other) {
        return equals(other, 1e-5);
    }

    public double getLength2D() {
        return UtilMath.rss(x, y);
    }

    public double getLength3D() {
        return UtilMath.rss(x, y, z);
    }

    public Point3DMutable minus(Point3DMutable other) {
        x -= other.x;
        y -= other.y;
        z -= other.z;
        return this;
    }

    public Point3DMutable add(Point3DMutable other) {
        x += other.x;
        y += other.y;
        z += other.z;
        return this;
    }

    public Point3DMutable add(double dx, double dy, double dz) {
        x += dx;
        y += dy;
        z += dz;
        return this;
    }

    public Point3DMutable minus(double dx, double dy, double dz) {
        x -= dx;
        y -= dy;
        z -= dz;
        return this;
    }

    public Point3DMutable add(Point2DMutable other) {
        this.add(other.getX(), other.getY(), 0);
        return this;
    }

    public Point3DMutable minus(Point2DMutable other) {
        this.minus(other.getX(), other.getY(), 0);
        return this;
    }

    public Point3DMutable scale(double s) {
        x *= s;
        y *= s;
        z *= s;
        return this;
    }

    public Point3D toImmutable() {
        return new Point3D(x, y, z);
    }

    public static Point3DMutable minus(Point3DMutable left, Point3DMutable right) {
        return new Point3DMutable(left.x - right.x, left.y - right.y, left.z - right.z);
    }

    public static Point3DMutable add(Point3DMutable left, Point3DMutable right) {
        return new Point3DMutable(left.x + right.x, left.y + right.y, left.z + right.z);
    }

    public static double calcLength2D(Point3DMutable p1, Point3DMutable p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        return UtilMath.rss(dx, dy);
    }

    public static double calcLength3D(Point3DMutable p1, Point3DMutable p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        double dz = p2.getZ() - p1.getZ();
        return UtilMath.rss(dx, dy, dz);
    }

    /**
     * @brief 点乘，求内积
     * @author Lucifer 2015-5-25
     */
    public static double dotProduct2D(Point3DMutable p1, Point3DMutable p2) {
        return p1.x * p2.x + p1.y * p2.y;
    }

    /**
     * @return 对于x为n，y为e，而言，这里z轴正方向是垂直于地球表面指向球内，因此返回值为正时， 正向看纸面，p1
     * 顺时针转一个小于180度的角，能到p2
     * @brief 叉乘
     * @author Lucifer 2015-5-28
     */
    public static double crossProduct2D(Point3DMutable p1, Point3DMutable p2) {
        return p1.x * p2.y - p1.y * p2.x;
    }

    public Point2DMutable toPoint2d() {
        Point2DMutable point2d = new Point2DMutable(this.x, this.y);
        return point2d;
    }

    public String toStringXy() {
        return "[" + x + "," + y + "]";
    }
}
