package com.xuzhouhhy.baidumap.data;

public class Line3D {

	private Point3DMutable a = new Point3DMutable();
	private Point3DMutable b = new Point3DMutable();
	public static double EP = 1E-5;

	public Line3D() {
		super();

	}

	public Line3D(Point3DMutable a, Point3DMutable b) {
		super();
		this.a = a;
		this.b = b;

	}

	public Point3DMutable getA() {
		return a;
	}

	public void setA(Point3DMutable a) {
		this.a = a;
	}

	public Point3DMutable getB() {
		return b;
	}

	public void setB(Point3DMutable b) {
		this.b = b;
	}

	public double getLength() {
		return Math.pow(
				((a.getX() - b.getX()) * (a.getX() - b.getX())
						+ (a.getY() - b.getY()) * (a.getY() - b.getY()) + (a
						.getZ() - b.getZ()) * (a.getZ() - b.getZ())), 0.5);
	}

	public double getChangeX() {
		return Math.abs(a.getX() - b.getX());
	}

	public double getChangeY() {
		return Math.abs(b.getY() - a.getY());
	}

	public double getChangeZ() {
		return Math.abs(b.getZ() - a.getZ());
	}

	public Line2D toLine2d() {
		Line2D line2d = new Line2D(this.a.toPoint2d(), this.b.toPoint2d());
		return line2d;
	}

	public Boolean isLineValid() {
		return !((Math.abs(a.getX() - b.getX()) < EP)
				&& (Math.abs(a.getY() - b.getY()) < EP) && (Math.abs(a.getZ()
				- b.getZ()) < EP));
	}
}
