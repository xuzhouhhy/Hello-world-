package com.xuzhouhhy.baidumap.data;

public class Line2D {
	private Point2DMutable a = new Point2DMutable();
	private Point2DMutable b = new Point2DMutable();
	public static double EP = 1E-5;

	public Line2D() {
		super();
	}

	public Line2D(Point2DMutable a, Point2DMutable b) {
		super();
		this.a = a;
		this.b = b;
	}

	public Point2DMutable getA() {
		return a;
	}

	public void setA(Point2DMutable a) {
		this.a = a;
	}

	public Point2DMutable getB() {
		return b;
	}

	public void setB(Point2DMutable b) {
		this.b = b;
	}

	public double getLength() {
		return Math.pow(
				((a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b
						.getY()) * (a.getY() - b.getY())), 0.5);
	}

	public double getChangeX() {
		return b.x - a.x;
	}

	public double getChangeY() {
		return b.y - a.y;
	}

	public double getChangeXY() {
		return a.y * b.x - a.x * b.y;
	}
	
	public Line3D toLine3d() {
		Line3D line3d = new Line3D(this.a.toPoint3d(), this.b.toPoint3d());
		return line3d;
	}

	public Boolean isLineValid() {
		return !((Math.abs(a.getX() - b.getX()) < EP) && (Math.abs(a.getY()
				- b.getY()) < EP));
	}
	
	

}
