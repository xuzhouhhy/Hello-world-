package com.xuzhouhhy.baidumap.data;

/**
 * @brief 直线的解析方程 a*x+b*y+c=0,法线为（a,b);方向为（-b,a）
 * @author Lucifer 2015-5-28
 */
public class Line2DEquation {
	private double a;
	private double b;
	private double c;
	public static double EP = 1E-10;

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}

	public Line2DEquation(Point2DMutable a, Point2DMutable b) {
		super();
		makeLine(a, b);
	}

	public Line2DEquation(Point2DMutable p, double k, int type) {
		super();
		makeLine(p, k, type);
	}

	public boolean isLineValid() {
		return !((Math.abs(a) < EP) && (Math.abs(b) < EP));
	}

	public boolean normalization() {
		if (!isLineValid()) {
			return false;
		} else {
			double length = Math.sqrt(a * a + b * b);
			a /= length;
			b /= length;
			c /= length;
			return true;
		}
	}

	public void makeLine(Point2DMutable p1, Point2DMutable p2) {
		a = p2.getY() - p1.getY();
		b = p1.getX() - p2.getX();
		c = p1.getY() * p2.getX() - p1.getX() * p2.getY();
	}

	/**
	 * @brief 一点+倾角或斜率
	 * @param type: 0 = slope angle 倾角 ,others = slope 斜率
	 * @author Lucifer 2015-5-28
	 */
	public void makeLine(Point2DMutable p, double k, int type) {
		if (type == 0) {
			a = Math.sin(k);
			b = -Math.cos(k);
			c = p.getY() * Math.cos(k) - p.getX() * Math.sin(k);
		} else {
			a = k;
			b = -1;
			c = p.getY() - k * p.getX();
		}
	}
}