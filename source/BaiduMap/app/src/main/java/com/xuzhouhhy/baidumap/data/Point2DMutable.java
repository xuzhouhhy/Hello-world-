package com.xuzhouhhy.baidumap.data;


import com.xuzhouhhy.baidumap.util.UtilMath;

import java.io.Serializable;


/**
 * @author Lucifer
 * @brief
 * @2015-3-19
 */
public class Point2DMutable implements Serializable {
    private static final long serialVersionUID = 1L;

    double x;
    double y;

    public Point2DMutable(double x, double y) {
        super();
        setValue(x, y);
    }

    public Point2DMutable() {
        this(0, 0);
    }

    public Point2DMutable(Point2DMutable other) {
        this(other.x, other.y);
    }

    public Point2DMutable(Point3DMutable other) {
        this(other.getX(), other.getY());
    }

    public void setValue(Point2DMutable other) {
        setValue(other.x, other.y);
    }

    public void setValue(Point3DMutable other) {
        setValue(other.getX(), other.getY());
    }

    public void setValue(double x, double y) {
        this.x = x;
        this.y = y;
    }


    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    public boolean equals(Point2DMutable other, double tolerance) {
        return (Math.abs(x - other.x) <= tolerance) &&
                (Math.abs(y - other.y) <= tolerance);
    }

    public boolean equals(Point2DMutable other) {
        return equals(other, 1e-5);
    }

    public double getLength() {
        return UtilMath.rss(x, y);
    }

    public Point2DMutable minus(Point2DMutable other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    public Point2DMutable minus(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Point2DMutable add(Point2DMutable other) {
        x += other.x;
        y += other.y;
        return this;
    }

    public Point2DMutable add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public static Point2DMutable minus(Point2DMutable left, Point2DMutable right) {
        return new Point2DMutable(left.x - right.x, left.y - right.y);
    }

    public static Point2DMutable add(Point2DMutable left, Point2DMutable right) {
        return new Point2DMutable(left.x + right.x, left.y + right.y);
    }

    /**
     * @brief 点乘，求内积
     * @author Lucifer 2015-5-25
     */
    public static double dotProduct(Point2DMutable p1, Point2DMutable p2) {
        return p1.x * p2.x + p1.y * p2.y;
    }

    /**
     * @return 对于x为n，y为e，而言，这里z轴正方向是垂直于地球表面指向球内，因此返回值为正时，
     * 正向看纸面，p1 顺时针转一个小于180度的角，能到p2
     * @brief 叉乘
     * @author Lucifer 2015-5-28
     */
    public static double crossProduct(Point2DMutable p1, Point2DMutable p2) {
        return p1.x * p2.y - p1.y * p2.x;
    }

    public static double calcLength(Point2DMutable p1, Point2DMutable p2) {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        return UtilMath.rss(dx, dy);
    }

    public Point3DMutable toPoint3d() {
        Point3DMutable point3d = new Point3DMutable(this.x, this.y, 0);
        return point3d;
    }

}
