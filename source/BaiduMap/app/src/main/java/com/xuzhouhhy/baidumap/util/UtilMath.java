package com.xuzhouhhy.baidumap.util;

import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;

import com.xuzhouhhy.baidumap.data.ErrorTemp;
import com.xuzhouhhy.baidumap.data.Line2D;
import com.xuzhouhhy.baidumap.data.Line2DEquation;
import com.xuzhouhhy.baidumap.data.Point2DMutable;
import com.xuzhouhhy.baidumap.data.Point3DMutable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucifer 2015-5-25
 * @brief
 */
public class UtilMath {

    public static final double EP = 1E-10;// 小于它的数认为是0
    public static final double MAX_PARAM = 1e15;

    public UtilMath() {
        super();
    }

    /**
     * 判断double值是否为nan或无穷
     */
    public static boolean isValuesNanOrInfinite(@NonNull double[] vals) {
        for (double val : vals) {
            if (isValueNanOrInfinite(val)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断double值是否为nan或无穷
     */
    public static boolean isValueNanOrInfinite(double val) {
        return Double.isNaN(val) || Double.isInfinite(val);
    }

    /**
     * @brief 角度规划到0~2Pi
     * @author Lucifer 2015-5-25
     */
    public static double getAngleIn2PI(double angle) {
        double tAngle = angle;
        if (tAngle < 0.0) {
            while (tAngle < 0.0) {
                tAngle += Math.PI * 2;
            }
            // in case round-off error bumps the value over
            if (tAngle >= Math.PI * 2) {
                tAngle = 0.0;
            }
        } else {
            while (tAngle >= Math.PI * 2) {
                tAngle -= Math.PI * 2;
            }
            // in case round-off error bumps the value under
            if (tAngle < 0.0)
                tAngle = 0.0;
        }
        return tAngle;
    }

    /**
     * @brief 角度规划到-PI~PI
     * @author Lucifer 2015-5-25
     */
    public static double getAngleInPI(double angle) {
        double tAngle = getAngleIn2PI(angle);
        if (tAngle > Math.PI) {
            tAngle = tAngle - Math.PI * 2;
        }
        return tAngle;
    }

    /**
     * @brief 角度规划到0~360
     * @author Lucifer 2015-5-25
     */
    public static double getAngleIn360(double deg) {
        if (deg < 0.0) {
            while (deg < 0.0) {
                deg += 360;
            }
            // in case round-off error bumps the value over
            if (deg >= 360) {
                deg = 0.0;
            }
        } else {
            while (deg >= 360) {
                deg -= 360;
            }
            // in case round-off error bumps the value under
            if (deg < 0.0)
                deg = 0.0;
        }
        return deg;
    }

    /**
     * @brief 角度规划到-180~180
     * @author Lucifer 2015-5-25
     */
    public static double getAngleIn180(double deg) {
        deg = getAngleIn360(deg);
        if (deg > 180) {
            deg = deg - 360;
        }
        return deg;
    }

    /**
     * 将秒规划到306度内，返回值为秒
     */
    public static double getSecIn360Degree(double sec) {
        double d = getAngleIn360(sec / 3600);
        return d * 3600;
    }

    /**
     * 计算向量到x轴的角,范围是0~2PI
     */
    public static double atanIn2PI(double y, double x) {
        double rad = Math.atan2(y, x);
        if (rad < 0) {
            rad += Math.PI * 2;
        }
        return rad;
    }

    /**
     * @param
     * @function calculate the square of the sum of aa^2,bb^2,cc^2
     */
    public static double rss(double aa, double bb, double cc) {
        double a = Math.abs(aa);
        double b = Math.abs(bb);
        double c = Math.abs(cc);
        if ((a > b) && (a > c)) {
            return a * Math.sqrt(1 + (b / a) * (b / a) + (c / a) * (c / a));
        }
        if ((b > a) && (b > c)) {
            return b * Math.sqrt(1 + (a / b) * (a / b) + (c / b) * (c / b));
        }
        if ((c > b) && (c > a)) {
            return c * Math.sqrt(1 + (b / c) * (b / c) + (a / c) * (a / c));
        }

        if (a == b) {
            if (b == c) {
                return a * Math.sqrt(3.0);
            }
            a *= Math.sqrt(2.0);
            if (a > c) {
                return a * Math.sqrt(1 + (c / a) * (c / a));
            } else {
                return c * Math.sqrt(1 + (a / c) * (a / c));
            }
        }
        if (a == c) {
            a *= Math.sqrt(2.0);
            if (a > b) {
                return a * Math.sqrt(1 + (b / a) * (b / a));
            } else {
                return b * Math.sqrt(1 + (a / b) * (a / b));
            }
        }
        if (b == c) {
            b *= Math.sqrt(3.0);
            if (b > a) {
                return b * Math.sqrt(1 + (a / b) * (a / b));
            } else {
                return a * Math.sqrt(1 + (b / a) * (b / a));
            }
        }

        return 0.0;
    }

    /**
     * @param
     * @function calculate the square of the sum of aa^2,bb^2
     */
    public static double rss(double aa, double bb) {
        return rss(aa, bb, 0.0);
    }

    public static boolean fuzzyEqual(double a, double b, double p) {
        return Math.abs(a - b) < p;
    }

    public static boolean isNonZero(double val) {
        return Math.abs(val) > EP;
    }

    public static double azimuth(Point2DMutable p1, Point2DMutable p2) {
        if (p1.equals(p2)) {
            return 0;
        } else {
            double angle = Math.atan2(p2.getY() - p1.getY(),
                    p2.getX() - p1.getX());
            return getAngleIn2PI(angle);
        }
    }

    public static double azimuth(Point3DMutable p1, Point3DMutable p2) {
        if (p1.equals(p2)) {
            return 0;
        } else {
            double angle = Math.atan2(p2.getY() - p1.getY(),
                    p2.getX() - p1.getX());
            return getAngleIn2PI(angle);
        }
    }

    public static double azimuth(Point2DMutable p) {
        double angle = Math.atan2(p.getY(), p.getX());
        return getAngleIn2PI(angle);
    }

    public static double azimuth(Point3DMutable p) {
        double angle = Math.atan2(p.getY(), p.getX());
        return getAngleIn2PI(angle);
    }

    /**
     * @return 该点与投影点的平距(左负右正）
     * @brief 计算点到直线上的投影点的坐标，返回
     * @author Lucifer 2015-5-29
     */
    public static double calcPedal(Point3DMutable st, Point3DMutable ed, Point3DMutable pos, Point3DMutable ret) {
        Point2DMutable a = new Point2DMutable(st);
        Point2DMutable b = new Point2DMutable(ed);
        Point2DMutable c = new Point2DMutable(pos);
        if (a.equals(b) || a.equals(c)) {
            ret.setValue(st);
            return 0;
        }
        if (b.equals(c)) {
            ret.setValue(ed);
            return 0;
        }
        Line2DEquation ab = new Line2DEquation(a, b);
        Point2DMutable d = normalVec(a, b, true);
        Line2DEquation ch = new Line2DEquation(c, d.add(c));
        Point2DMutable pedal = new Point2DMutable();
        boolean ok = false;
        if (ab.isLineValid() && ch.isLineValid()) {
            ok = planeLineIntersection(ab, ch, pedal);
        }
        if (!ok) {
            return 0;
        }

        double s = propotionOfAPToAB(a, b, pedal);
        double z = st.getZ() + (ed.getZ() - st.getZ()) * s;
        ret.setValue(pedal.getX(), pedal.getY(), z);

        double dist = Math.abs(ab.getA() * pos.getX() + ab.getB() * pos.getY()
                + ab.getC())
                / rss(ab.getA(), ab.getB());

        Point2DMutable stEd = b.minus(a);
        Point2DMutable stP = c.minus(a);
        double cross = Point2DMutable.crossProduct(stEd, stP);
        double f = 1.0;
        if (cross < -1e-6) {
            f = -1.0;
        }
        dist *= f;

        return dist;
    }

    /**
     * @brief 计算等长法向量，坐标系为地里平面坐标系（x轴垂直向上，y轴垂直向右）
     * @author Lucifer 2015-5-28
     */
    public static Point2DMutable normalVec(Point2DMutable a, Point2DMutable b, Boolean clockWise) {
        double dx = b.getX() - a.getX();
        double dy = -(b.getY() - a.getY());
        if (!clockWise) {
            dx = -dx;
            dy = -dy;
        }
        return new Point2DMutable(dy, dx);
    }

    /**
     * @brief 计算单位向量
     * @author Lucifer 2015-5-28
     */
    public static Point2DMutable unitVec(Point2DMutable a, Point2DMutable b) {
        double dist = Point2DMutable.calcLength(a, b);
        Point2DMutable p = new Point2DMutable();
        if (dist > 0) {
            p.setX((b.getX() - a.getX()) / dist);
            p.setY((b.getY() - a.getY()) / dist);
        }
        return p;
    }

    /**
     * @brief : 线段旋转90度，坐标系为地里平面坐标系（x轴垂直向上，y轴垂直向右） @
     * @date:2013-8-26
     * @author:Lucifer
     */
    public static Point2DMutable rotateBy90(Point2DMutable st, Point2DMutable ed, Boolean clockWise) {
        Point2DMutable p = normalVec(st, ed, clockWise);
        return p.add(st);
    }

    /**
     * @param line1 ,line2: 2D line( coordinate reference system: right hand 2D
     *              Cartesian )
     * @param point :the intersection point
     * @function calculate intersected point coordinate of 2 lines
     * @return: true = two line is intersected,others two is parallel
     * @date:2012-5-31
     * @author:Lucifer
     */
    public static boolean planeLineIntersection(Line2DEquation line1,
                                                Line2DEquation line2, Point2DMutable point) {
        double a = line1.getA() * line2.getB() - line2.getA()
                * line1.getB();
        if (Math.abs(a) < EP) {
            return false;
        } else {
            point.setX((line1.getB() * line2.getC() - line2.getB()
                    * line1.getC())
                    / a);
            point.setY(-(line1.getA() * line2.getC() - line2.getA()
                    * line1.getC())
                    / a);
            return true;
        }
    }

    /**
     * @param a ,b,p:in one line return s: AP:AB( s is negative if p is in the
     *          inverse extension chord of A )
     * @function
     * @date:2012-12-27
     * @author:Lucifer
     */
    public static double propotionOfAPToAB(Point2DMutable a, Point2DMutable b, Point2DMutable p) {
        if (a.equals(b)) {
            return 0.0;
        }
        Point2DMutable ab = Point2DMutable.minus(b, a);
        Point2DMutable ap = Point2DMutable.minus(p, a);
        double d = Math.abs((ab.getX() * ap.getY() - ab.getY() * ap.getX()));
        return (ab.getX() * ap.getX() + ab.getY() * ap.getY())
                / (ab.getX() * ab.getX() + ab.getY() * ab.getY());
//        if (d > 1e-2) {//应该是计算失败的
//            return 0.0;
//        }
    }

    /**
     * @brief : 计算投影长度，延长线上返回赋值 @
     * @date:2013-8-26
     * @author:Lucifer
     */
    public static double projectionLenth(Point2DMutable st, Point2DMutable ed, Point2DMutable p) {

        if (st.equals(ed)) {
            return 0.0;
        } else {
            Point2DMutable se = Point2DMutable.minus(ed, st);
            Point2DMutable sp = Point2DMutable.minus(p, st);
            return Point2DMutable.dotProduct(se, sp) / se.getLength();
        }
    }

    /**
     * 计算点在线上的投影是否在线内
     */
    public static boolean isProjectInsideLine(Point2DMutable st, Point2DMutable ed, Point2DMutable p) {
        if (st.equals(ed)) {
            return true;
        } else {
            double prjLength = projectionLenth(st, ed, p);
            if (prjLength < 0) {
                return false;
            } else {
                return prjLength <= Distance(st, ed);
            }
        }
    }

    /**
     * @brief ：计算点与线的偏距，右侧为正
     * @date:2014-5-24
     * @author:Lucifer
     */
    public static double distToLine(Point2DMutable st, Point2DMutable ed, Point2DMutable target) {
        if (st.equals(ed)) {
            return Distance(st, target);
        } else {
            Line2DEquation l = new Line2DEquation(st, ed);
            double dist = Math.abs(l.getA() * target.getX() + l.getB()
                    * target.getY() + l.getC())
                    / rss(l.getA(), l.getB());

            Point2DMutable stEd = Point2DMutable.minus(ed, st);
            Point2DMutable stP = Point2DMutable.minus(target, st);
            double cross = Point2DMutable.crossProduct(stEd, stP);
            double f = 1.0;
            if (cross < -1e-6) {
                f = -1.0;
            }
            return dist * f;
        }
    }

    /**
     * @function : 线段沿法线方向平移一段距离，返回平移量
     * @date:2012-8-21
     * @author:Lucifer
     * @formula reference:<<Geometric Tools for Computer Graphics>> by Philip
     * J.Schneider David H.Eberly 周长发译，2005.1 出版(章节：8.1.4）
     */
    public static boolean offsetOfParalleToGivenLineAtGiverDistance(Point2DMutable st,
                                                                    Point2DMutable end, double dist, Point2DMutable ret) {
        Line2DEquation l = new Line2DEquation(st, end);
        if (l.normalization()) {
            Point2DMutable n = new Point2DMutable(l.getA(), l.getB());
            double da = azimuth(n) - azimuth(st, end);
            if (Math.abs(getAngleIn2PI(da) - Math.PI / 2) > 1e-4) {
                dist = -dist;
            }
            ret.setX(l.getA() * dist);
            ret.setY(l.getB() * dist);
            return true;
        } else {

            return false;
        }
    }

    /**
     * @param a                :3D coordinate of point A( coordinate reference system: right
     *                         hand 3D Cartesian )
     * @param planeDistance    : the plane geometrical distance P to A,
     * @param verticalDistance : vertical distance of P to A(sighed)
     * @param azimuth          : the angle of vector AP rotated to X-axis
     *                         clockwise,rang(0,2*PI),unit(radian)
     * @function calculate coordinate of point P
     * @date:2012-5-31
     * @author:Lucifer
     */
    public static Point3DMutable eccentricSurvey(Point3DMutable a, double planeDistance,
                                                 double verticalDistance, double azimuth) {
        Point3DMutable p = new Point3DMutable();
        double x = a.getX() + planeDistance * Math.cos(azimuth);
        double y = a.getY() + planeDistance * Math.sin(azimuth);
        double z = a.getZ() + verticalDistance;
        p.setX(x);
        p.setY(y);
        p.setZ(z);
        return p;
    }

    /**
     * @param a :known point A
     * @param b :known point B
     * @param s : |AP|:|AB| ( s is negative if p is in the inverse extension
     *          chord of AB,is between o and 1 if p is between A and B )
     * @function calculate a point coordinate in one line
     * @date:2012-6-5
     * @author:Lucifer
     */
    public static Point3DMutable pointInLine(Point3DMutable a, Point3DMutable b, double s) {
        if (a.equals(b)) {
            return null;
        } else {
            Point3DMutable p = new Point3DMutable();
            p.setX(a.getX() + s * (b.getX() - a.getX()));
            p.setY(a.getY() + s * (b.getY() - a.getY()));
            p.setZ(a.getZ() + s * (b.getZ() - a.getZ()));
            return p;
        }
    }

    /**
     * @param a    :known point A
     * @param b    :known point B
     * @param dist : ap,( directin AB )
     * @function calculate a point coordinate in one line
     * @date:2013-08-21
     * @author:Lucifer
     */
    public static Point2DMutable pointInLine(Point2DMutable a, Point2DMutable b, double dist) {
        if (a.equals(b)) {
            return null;
        } else {
            Line2DEquation l = new Line2DEquation(a, b);
            l.normalization();

            double da = azimuth(a, b)
                    - azimuth(new Point2DMutable(-l.getB(), l.getA()));
            if (Math.abs(da) > 1e-4) {// 保证方向向量为AB
                dist = -dist;
            }
            double dx = -dist * l.getB();
            double dy = dist * l.getA();
            return new Point2DMutable(a.getX() + dx, a.getY() + dy);
        }
    }

    /**
     * @param rotation :the angle from AB to AP counter-clockwise
     * @function calculate the coordinate of Point P
     * @date:2012-6-13
     * @author:Lucifer
     */
    public static Point2DMutable pointInRotatedLine(Point2DMutable a, Point2DMutable b,
                                                    double rotation, double ap) {
        Point2DMutable p = offsetVectorToLine(a, b, rotation, ap);
        if (null != p) {
            p.add(a);
        }
        return p;
    }

    /**
     * @param rotation :the angle from AB to AP counter-clockwise
     * @function calculate the vector AP
     * @date:2012-6-13
     * @author:Lucifer
     */
    public static Point2DMutable offsetVectorToLine(Point2DMutable a, Point2DMutable b,
                                                    double rotation, double ap) {
        if (a.equals(b)) {
            return null;
        } else {
            double ab = azimuth(a, b);
            double apAzimuth = getAngleIn2PI(ab + rotation);
            return new Point2DMutable(ap * Math.cos(apAzimuth), ap * Math.sin(apAzimuth));
        }
    }

    /**
     * @param a : point A
     * @param b : point B
     * @param c : point C
     * @function calculate the deflection angle from AB to BC clockwise
     * @date:2012-6-5
     * @author:Lucifer
     * @return:[0,2*pi)
     */
    public static double deflectionAngle(Point2DMutable a, Point2DMutable b, Point2DMutable c) {
        if (a.equals(b) || b.equals(c) || a.equals(c)) {
            return 0.0;
        } else {
            double abAzimuth = azimuth(a, b);
            double bcAzimuth = azimuth(b, c);
            double angle = getAngleIn2PI(bcAzimuth - abAzimuth);
            if (angle >= Math.PI) {
                angle = angle - 2 * Math.PI;
            }
            return angle;
        }
    }

    /**
     * @brief 向量vec旋转方位角dAzi
     * @author Lucifer 2015-6-2
     */
    public static void rotate(Point2DMutable ret, Point2DMutable vec, double dAzi) {
        double cos = Math.cos(dAzi);
        double sin = Math.sin(dAzi);
        ret.setValue(vec.getX() * cos - vec.getY() * sin, vec.getX() * sin + vec.getY() * cos);
    }

    /**
     * @param L           到起点的长度
     * @param Ls          曲线长度
     * @param curvatureSt 起点曲率
     * @param curvatureEd 终点曲率
     * @param pre         坐标精度
     * @brief 根据里程计算缓和曲线中点坐标的一个步骤
     * @author Lucifer 2015-6-3
     */
    public static double spiralX(double L, double Ls, double curvatureSt, double curvatureEd, double pre) {
        if (Math.abs(Ls) < 1e-3 || fuzzyEqual(curvatureSt, curvatureEd, 1e-6)) {
            return 0;
        }
        double xt0 = -1;
        double xt1 = -2;
        double xt = L;
        double dA = curvatureSt;
        double dB = (curvatureEd - curvatureSt) * 0.5 / Ls;
        int k = 2;
        while (k < 26) {
            for (int j = ((k + 3) / 4) * 2; j <= k; j += 2) {
                int n = j;
                int i = k - j;
                int s = 1;
                if ((j / 2) % 2 == 1) {
                    s = -1;
                }
                double t = Math.pow(dA, n - i) * Math.pow(dB, i) * Math.pow(L, k + 1) /
                        (fact(n - i) * fact(i) * (k + 1.0));
                xt += s * t;

            }
            if (fuzzyEqual(xt0 - xt1, xt1 - xt, pre) && k > 13) {
                break;
            }
            xt0 = xt1;
            xt1 = xt;
            k++;
        }
        return xt;
    }

    /**
     * @param L           到起点的长度
     * @param Ls          曲线长度
     * @param curvatureSt 起点曲率
     * @param curvatureEd 终点曲率
     * @param pre         坐标精度
     * @brief 根据里程计算缓和曲线中点坐标的一个步骤
     * @author Lucifer 2015-6-3
     */
    public static double spiralY(double L, double Ls, double curvatureSt, double curvatureEd, double pre) {
        if (Math.abs(Ls) < 1e-3 || fuzzyEqual(curvatureSt, curvatureEd, 1e-6)) {
            return 0;
        }
        double yt = 0;
        double yt0 = -1;
        double yt1 = -2;
        double dA = curvatureSt;
        double dB = (curvatureEd - curvatureSt) * 0.5 / Ls;
        int k = 1;
        while (k < 30) {
            for (int j = ((k + 1) / 4) * 2 + 1; j <= k; j += 2) {
                int n = j;
                int i = k - j;
                int s = 1;
                if ((j / 2) % 2 == 1) {
                    s = -1;
                }
                double t = Math.pow(dA, n - i) * Math.pow(dB, i) * Math.pow(L, k + 1) /
                        (fact(n - i) * fact(i) * (k + 1.0));
                yt += s * t;
            }
            if (fuzzyEqual(yt0 - yt1, yt1 - yt, pre) && k > 15) {
                break;
            }
            yt0 = yt1;
            yt1 = yt;
            k++;
        }
        return yt;
    }

    private static double fact(int i) {
        return factArray[i];
    }

    private static final double[] factArray =
            {
                    1.0,
                    1.0,                                    // 1!
                    2.0,                                    // 2!
                    6.0,                                    // 3!
                    24.0,                                    // 4!
                    120.0,                                    // 5!
                    720.0,                                    // 6!
                    5040.0,                                    // 7!
                    40320.0,                                // 8!
                    362880.0,                                // 9!
                    3628800.0,                                // 10!
                    39916800.0,                                // 11!
                    479001600.0,                            // 12!
                    6227020800.0,                            // 13!
                    87178291200.0,                            // 14!
                    1307674368000.0,                        // 15!
                    20922789888000.0,                        // 16!
                    355687428096000.0,                        // 17!
                    6402373705728000.0,                        // 18!
                    121645100408832000.0,                    // 19!
                    2432902008176640000.0,                    // 20!
                    51090942171709440000.0,                    // 21!
                    1124000727777607680000.0,                // 22!
                    25852016738884976640000.0,                // 23!
                    620448401733239439360000.0,                // 24!
                    15511210043330985984000000.0,            // 25!
                    403291461126605635584000000.0,            // 26!
                    10888869450418352160768000000.0,        // 27!
                    304888344611713860501504000000.0,        // 28!
                    8841761993739701954543616000000.0,        // 29!
                    265252859812191058636308480000000.0,    // 30!
                    8222838654177922817725562880000000.0,    // 31!
                    263130836933693530167218012160000000.0    // 32!
            };


    public static double PI_OVER_2 = 0.5 * Math.PI;
    public static double PI_OVER_4 = 0.25 * Math.PI;

    /**
     * @param a    point1
     * @param b    point2
     * @param type 0=space distance,1=plane distance,2=vertical distance
     * @function calculate distance between two points
     */
    public static double Distance(Point3DMutable a, Point3DMutable b, int type) {
        if (type == 0) {
            return Rss(a.getX() - b.getX(), a.getY() - b.getY(),
                    a.getZ() - b.getZ());
        } else if (type == 1) {

            return Rss(a.getX() - b.getX(), a.getY() - b.getY());
        } else {
            return b.getZ() - a.getZ();
        }

    }

    public static double Distance(Point2DMutable a, Point2DMutable b) {
        return Rss(a.getX() - b.getX(), a.getY() - b.getY());
    }

    /**
     * @param
     * @function calculate the square of the sum of aa^2,bb^2,cc^2
     */
    private static double Rss(double aa, double bb, double cc) {
        double a = Math.abs(aa);
        double b = Math.abs(bb);
        double c = Math.abs(cc);
        if ((a > b) && (a > c)) {
            return a * Math.sqrt(1 + (b / a) * (b / a) + (c / a) * (c / a));
        }
        if ((b > a) && (b > c)) {
            return b * Math.sqrt(1 + (a / b) * (a / b) + (c / b) * (c / b));
        }
        if ((c > b) && (c > a)) {
            return c * Math.sqrt(1 + (b / c) * (b / c) + (a / c) * (a / c));
        }

        if (a == b) {
            if (b == c) {
                return a * Math.sqrt(3.0);
            }
            a *= Math.sqrt(2.0);
            if (a > c) {
                return a * Math.sqrt(1 + (c / a) * (c / a));
            } else {
                return c * Math.sqrt(1 + (a / c) * (a / c));
            }
        }
        if (a == c) {
            a *= Math.sqrt(2.0);
            if (a > b) {
                return a * Math.sqrt(1 + (b / a) * (b / a));
            } else {
                return b * Math.sqrt(1 + (a / b) * (a / b));
            }
        }
        if (b == c) {
            b *= Math.sqrt(2.0);
            if (b > a) {
                return b * Math.sqrt(1 + (a / b) * (a / b));
            } else {
                return a * Math.sqrt(1 + (b / a) * (b / a));
            }
        }

        return 0.0;
    }

    /**
     * @param
     * @function calculate the square of the sum of aa^2,bb^2
     */
    private static double Rss(double aa, double bb) {
        return Rss(aa, bb, 0.0);
    }

    /**
     * @param
     * @function 计算点到直线上的投影点的坐标，返回该点与投影点的平距
     */
    public static double CalcPedal(Point3DMutable a, Point3DMutable b, Point3DMutable c) {
        if (a.equals(b)) {
            return 0.0;
        } else if ((a.equals(c)) || (c.equals(b))) {
            return 0.0;
        } else {
            Line2D ab = new Line2D(a.toPoint2d(), b.toPoint2d());
            return DistanceOfPointToLine(c.toPoint2d(), ab);
        }
    }

    public void SplitLineByFixedLength(Point3DMutable lineBegin, Point3DMutable lineEnd,
                                       double length, List<Point3DMutable> points) {
        points.clear();
        double startx = lineBegin.getX();
        double starty = lineBegin.getY();
        double startz = lineBegin.getZ();
        double dx = lineEnd.getX() - startx;
        double dy = lineEnd.getY() - starty;
        double dz = lineEnd.getZ() - startz;

        double lineLength = Distance(lineBegin, lineEnd, 1);
        if (lineLength <= length) {
            return;
        }
        int num = (int) (lineLength / length);

        Point3DMutable tmpPoint = new Point3DMutable();
        for (int i = 1; i < num; ++i) {
            tmpPoint.setX(startx + dx * i / num);
            tmpPoint.setY(starty + dy * i / num);
            tmpPoint.setZ(startz + dz * i / num);
            points.add(tmpPoint);
        }
    }

    public void SplitLineByFixedNum(Point3DMutable lineBegin, Point3DMutable lineEnd,
                                    double num, List<Point3DMutable> points) {
        points.clear();
        double startx = lineBegin.getX();
        double starty = lineBegin.getY();
        double startz = lineBegin.getZ();
        double dx = lineEnd.getX() - startx;
        double dy = lineEnd.getY() - starty;
        double dz = lineEnd.getZ() - startz;

        double lineLength = Distance(lineBegin, lineEnd, 1);
        if (num <= 1) {
            return;
        }
        Point3DMutable tmpPoint = new Point3DMutable();
        for (int i = 1; i < num; ++i) {
            tmpPoint.setX(startx + dx * i / num);
            tmpPoint.setY(starty + dy * i / num);
            tmpPoint.setZ(startz + dz * i / num);
            points.add(tmpPoint);
        }
    }

    /**
     * @param a :known point A
     * @param b :known point B
     * @param s : |AP|:|AB| ( s is negative if p is in the inverse extension
     *          chord of AB,is between o and 1 if p is between A and B )
     * @function calculate a point coordinate in one line
     * @date:2012-6-5
     * @author:Lucifer
     */
    public static Point3DMutable PointInLine(Point3DMutable a, Point3DMutable b, double s) {
        if (a.equals(b)) {
            return null;
        } else {
            Point3DMutable p = new Point3DMutable();
            p.setX(a.getX() + s * (b.getX() - a.getX()));
            p.setY(a.getY() + s * (b.getY() - a.getY()));
            p.setZ(a.getZ() + s * (b.getZ() - a.getZ()));
            return p;
        }
    }

    /**
     * @function calculate the plane distance of AB
     * @date:2012-6-12
     * @author:Lucifer
     */
    public static double PlaneDistance(Point2DMutable a, Point2DMutable b) {
        return Rss(b.getX() - a.getX(), b.getY() - a.getY());
    }

    /**
     * @function calculate the slope distance of AB
     * @date:2012-6-12
     * @author:Lucifer
     */
    public static double SlopeDistance(Point3DMutable a, Point3DMutable b) {
        if (a.equals(b)) {
            return 0.0;
        } else {

            return Rss(b.getX() - a.getX(), b.getY() - a.getY(),
                    b.getZ() - a.getZ());
        }
    }

    /**
     * @function calculate the elevation of AB( rang( -PI/2,PI/2 ),the sign is
     * '+' if B is higher than A)
     * @date:2012-6-12
     * @author:Lucifer
     */
    public static double Elevation(Point3DMutable a, Point3DMutable b) {
        if (a.equals(b)) {
            return 0.0;
        } else {

            double p = PlaneDistance(new Point2DMutable(a.getX(), a.getY()),
                    new Point2DMutable(b.getX(), b.getY()));
            double v = b.getZ() - a.getZ();
            double temp = Math.atan2(v, p);
            if (temp > PI_OVER_2) {
                temp -= PI_OVER_2;
            } else if (temp < -PI_OVER_2) {
                temp += PI_OVER_2;
            }

            return temp;
        }
    }

    /**
     * @function calculate the gradient of AB( rang( -1,1 ) ),the sign is '+' if
     * B is higher than A)
     * @date:2012-6-12
     * @author:Lucifer
     */
    public static double Gradient(Point3DMutable a, Point3DMutable b) {
        if (a.equals(b)) {
            return 0.0;
        } else {
            double p = PlaneDistance(new Point2DMutable(a.getX(), a.getY()),
                    new Point2DMutable(b.getX(), b.getY()));
            double v = b.getZ() - a.getZ();
            return v / p;
        }
    }

    /**
     * @function calculate the coordinate of Point P which is in the bisector of
     * angle ABC(rang(0,180](if ABC = 180,BA rotates 90 to the
     * bisector) bp:the distance of Point P to B(bp is negative is p
     * is outside angle ABC)
     * @date:2012-6-13
     * @author:Lucifer
     */
    public static Point2DMutable PointInBisector(Point2DMutable a, Point2DMutable b, Point2DMutable c,
                                                 double bp) {
        if (a.equals(b)) {
            return null;
        } else if (c.equals(b)) {
            return null;
        } else if (a.equals(c)) {
            return null;
        } else {
            double ba = azimuth(b, a);
            double bc = azimuth(b, c);
            double d = (bc + ba) / 2;
            if (Math.abs(bc - ba) > Math.PI) {
                d = getAngleIn2PI(d + Math.PI);
            }
            Point2DMutable p = new Point2DMutable();
            double x = b.getX() + bp * Math.cos(d);
            double y = b.getY() + bp * Math.sin(d);
            p.setX(x);
            p.setY(y);
            return p;
        }
    }

    /**
     * @function calculate coordinate of Point P with two point and two side
     * @return:return the calculated coordinate in the left side of vector AB
     * (对于横轴为y轴的坐标系而言,P求出的是AB右侧的点）
     * @date:2012-6-14
     * @author:Lucifer
     * @formula reference:<<Geometric Tools for Computer Graphics>> by Philip
     * J.Schneider David H.Eberly 周长发译，2005.1 出版章节：7.5.2）
     */
    public static Point2DMutable PointOfSideIntersetion(Point2DMutable a, Point2DMutable b,
                                                        double ap, double bp) {

        Point2DMutable aPoint = new Point2DMutable();
        if (a.equals(b)) {
            return null;
        } else {
            double ab = PlaneDistance(a, b);
            double sum = ap + bp;
            double diff = Math.abs(ap - bp);

            if ((ab > sum) || (ab < diff)) {
                ErrorTemp.IntersectionError2 = 2;
                return aPoint;
            }

            Boolean b1 = (Math.abs(sum - ab) < EP);// 两个是否圆外切
            Boolean b2 = (Math.abs(diff - ab) < EP);// 两个是否圆内切

            if (b1 || b2) {
                double s = ap / ab;
                if (b2 && (ap < bp)) {
                    s = -s;
                }

                aPoint.setX(a.getX() + s * (b.getX() - a.getX()));
                aPoint.setY(a.getY() + s * (b.getY() - a.getY()));
                return aPoint;
            }

            // 两个圆相交
            double dx = b.getX() - a.getX();
            double dy = b.getY() - a.getY();
            double abSq = dx * dx + dy * dy;
            double s = (ap * ap - bp * bp) / (2 * abSq) + 0.5;
            double t = -Math.sqrt(ap * ap / abSq - s * s);

            aPoint.setX(a.getX() + s * dx + t * (-dy));
            aPoint.setY(a.getY() + s * dy + t * dx);
            return aPoint;
        }
    }

    /**
     * @param s :ordered vertexes of a simple polygon clockwise or
     *          anticlockwise
     * @function : calculate area of a simple polygon
     * @date:2012-6-20
     * @author:Lucifer
     * @formula reference:<<Geometric Tools for Computer Graphics>> by Philip
     * J.Schneider David H.Eberly 周长发译，2005.1 出版(章节：13.12.1）
     */
    public static double SimplePolygonArea(Point2DMutable[] s) {
        double area = 0.0;
        int n = s.length;
        if (n >= 3) {
            area += s[0].getX() * (s[1].getY() - s[n - 1].getY());// 公式的首项
            area += s[n - 1].getX() * (s[0].getY() - s[n - 2].getY());// 公式的尾项
            for (int i = 1; i < n - 1; i++) {
                area += s[i].getX() * (s[i + 1].getY() - s[i - 1].getY());
            }

        }
        return Math.abs(0.5 * area);
    }

    /**
     * @param s :ordered vertexes of a simple polygon clockwise or
     *          anticlockwise
     * @function : calculate perimeter of a simple polygon
     * @date:2012-6-20
     * @author:Lucifer
     */
    public static double PolygonPerimeter(Point2DMutable[] s) {
        double perimeter = 0.0;
        int n = s.length;
        if (n >= 3) {
            perimeter += PlaneDistance(s[0], s[n - 1]);
            for (int i = 0; i < n - 1; i++) {
                perimeter += PlaneDistance(s[i], s[i + 1]);
            }
        }
        return perimeter;
    }

    /**
     * @function : calculate the distance from one point to line
     * @date:2012-6-27
     * @author:Lucifer
     * @formula reference:<<Geometric Tools for Computer Graphics>> by Philip
     * J.Schneider David H.Eberly 周长发译，2005.1 出版(章节：6.1.1）
     */
    private static double DistanceOfPointToLine(Point2DMutable point, Line2D line) {
        if (line.isLineValid() == true) {
            return Math.abs(line.getChangeY()
                    * point.getX()
                    + line.getChangeX()
                    * point.getY()
                    + (line.getA().getY() * line.getB().getX() - line.getA()
                    .getX() * line.getB().getY()))
                    / Rss(line.getChangeY(), line.getChangeX());
        } else {
            return 0.0;
        }
    }

    /**
     * @param
     * @return
     * @brief 在ref_angle基础上旋转angle弧度
     * @date 2012-9-5 14:49:00
     */
    public double CCW(double ref_angle, double angle) {
        double fangle1 = getAngleIn2PI(angle);
        double fref = getAngleIn2PI(ref_angle);

        if (fref > fangle1) {
            return getAngleIn2PI(2 * Math.PI - (fref - fangle1));
        } else
            return getAngleIn2PI(fangle1 - fref);
    }

    /**
     * @param
     * @return
     * @brief 取一个浮点数的符号
     * @date 2012-9-5 14:49:42
     */
    public static int Sign(double d) {
        if (d > 0) {
            return 1;
        } else if (d < 0) {
            return -1;
        } else {
            return 0;
        }
    }


    /**
     * @brief : 计算等长法向量，坐标系为地里平面坐标系（x轴垂直向上，y轴垂直向右） @
     * @date:2013-8-26
     * @author:Lucifer
     */
    public static Point2DMutable norlmalVec(Point2DMutable a, Point2DMutable b, Boolean bool) {
        double dx = b.getX() - a.getX();
        double dy = -(b.getY() - a.getY());
        if (!bool) {
            dx = -dx;
            dy = -dy;
        }

        return new Point2DMutable(dy, dx);

    }

    /**
     * @param :sec 子午线经度,单位为秒,范围在-180~180
     * @return -1,表示错误
     * @brief : 计算UTM投影6度带带号
     * @date:2013-9-23
     * @author:Lucifer
     */
    public static int zoneOfMedidian_UTM6(double sec) {
        if (sec < -180 * 3600 || sec > 180 * 3600) {
            return -1;
        }
        double degree = sec / 3600;
        int zone = (int) Math.floor(degree / 6) + 31;
        if (zone > 60) {
            zone -= 60;
        }
        return zone;
    }

    /**
     * @param : zone,带号,范围在1~60
     * @brief : 根据带号,计算UTM投影6度带中央子午线,30和31度带的零界点是0度，东（西）经180度开始，自西向东为1~60带
     * @retval : 中央子午线经度,单位为秒
     * @date:2013-9-23
     * @author:Lucifer
     */
    public static double centralMeidianOfZone_UTM6(int zone) {
        return (-180 + (6 * zone - 3)) * 3600;
    }

    public static void ls_polygon_isect_line(Point2DMutable p1, Point2DMutable p2,
                                             Point2DMutable pos, int winding) {
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        double y = pos.getY();

        int dir = 1;
        if (fuzzyCompare(y1, y2)) {

            return;
        } else if (y2 < y1) {
            double x_tmp = x2;
            x2 = x1;
            x1 = x_tmp;
            double y_tmp = y2;
            y2 = y1;
            y1 = y_tmp;
            dir = -1;
        }

        if (y >= y1 && y < y2) {
            double x = x1 + ((x2 - x1) / (y2 - y1)) * (y - y1);

            // count up the winding number if we're
            if (x <= pos.getX()) {
                winding += dir;
            }
        }
    }

    /**
     * @param polygon 多边形点序列
     * @param pt      当前点
     * @return true-inside false-outside
     * @function 判断一个点是否在多边形内
     * @author Rochy
     * @date 2013-10-17 14:43:32
     */
    public static Boolean isPointInsidePolygon(ArrayList<Point2DMutable> polygon,
                                               Point2DMutable pt, Boolean winding) {
        if (polygon.size() == 0) {
            return false;
        } else {
            int winding_number = 0;
            Point2DMutable last_pt = polygon.get(0);
            Point2DMutable last_start = polygon.get(0);

            for (int i = 1; i < polygon.size(); ++i) {
                Point2DMutable e = new Point2DMutable();
                e = polygon.get(i);
                ls_polygon_isect_line(last_pt, e, pt, winding_number);
                last_pt = e;
            }
            if (!(last_pt == last_start)) {
                ls_polygon_isect_line(last_pt, last_start, pt, winding_number);
            }

            return (winding ? (winding_number != 0)
                    : ((winding_number % 2) != 0));
        }
    }

    /**
     * @return true if p1 equals p2
     * @brief compare two double value considering the magnitude
     */
    public static Boolean fuzzyCompare(double p1, double p2) {

        if (Math.abs(p1 - p2) <= 0.000000000001 * Math.min(Math.abs(p1),
                Math.abs(p2))) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * deprecated
     *
     * @param j     ,塔基上一个桩
     * @param angle 偏角，单位为度
     * @return 返回4个塔脚的坐标
     * @brief 计算塔的4个塔脚坐标
     * @parma o, 塔基中桩
     * @parma a, b 长和宽
     * @date:2014-5-6
     * @author:Lucifer
     * @ref 南方电力之星2.0
     */
    public static ArrayList<Point2DMutable> calcRectangleTowerFeet(Point2DMutable j,
                                                                   Point2DMutable o, double a, double b, double angle) {

        if (a > 0 || b > 0 || b < a || !(o == j)) {
            return null;
        } else {
            double lengOfOj = Distance(j, o);
            double cosW = (j.getX() - o.getX()) / lengOfOj;
            double sinW = (j.getY() - o.getY()) / lengOfOj;
            double job = (angle + 90) * Math.PI / 180;
            double cosJOB = Math.cos(job);
            double sinJOB = Math.sin(job);
            Point2DMutable oB = new Point2DMutable();
            oB.setX(cosW * cosJOB * a - sinW * sinJOB * a);
            oB.setY(sinW * cosJOB * a + cosW * sinJOB * a);
            Point2DMutable B = new Point2DMutable(oB.getX() + o.getX(), oB.getY() + o.getY());
            double sinBOC = 2 * b * Math.sqrt(a * a - b * b) / (a * a);
            double cosBOC = 2 * b * b / (a * a) - 1;
            Point2DMutable oC = new Point2DMutable();
            oC.setX(oB.getX() * cosBOC - oB.getY() * sinBOC);
            oC.setY(oB.getY() * cosBOC + oB.getX() * sinBOC);
            Point2DMutable C = new Point2DMutable(oC.getX() + o.getX(), oC.getY() + o.getY());

            Point2DMutable A = new Point2DMutable(2 * o.getX() - C.getX(), 2 * o.getY()
                    - C.getY());
            Point2DMutable D = new Point2DMutable(2 * o.getX() - B.getX(), 2 * o.getY()
                    - B.getY());
            ArrayList<Point2DMutable> ret = new ArrayList<Point2DMutable>();
            ret.add(A);
            ret.add(B);
            ret.add(C);
            ret.add(D);

            return ret;
        }
    }

    /**
     * deprecated
     *
     * @param j     ,塔基上一个桩
     * @param angle 偏角，单位为度
     * @return 返回4个塔脚的坐标
     * @brief 计算方形塔的4个塔脚坐标
     * @parma o, 塔基中桩
     * @parma a 为对角线半长
     * @date:2014-5-6
     * @author:Lucifer
     * @ref 南方电力之星2.0
     */
    public static ArrayList<Point2DMutable> calcSquareTowerFeet(Point2DMutable j, Point2DMutable o,
                                                                double a, double angle) {
        if (a > 0 || !(o == j)) {
            return null;
        } else {
            double lengOfOj = Distance(j, o);
            double cosW = (j.getX() - o.getX()) / lengOfOj;
            double sinW = (j.getY() - o.getY()) / lengOfOj;
            double joa = (angle + 45) * Math.PI / 180;
            double cosJOA = Math.cos(joa);
            double sinJOA = Math.sin(joa);
            Point2DMutable oA = new Point2DMutable();
            oA.setX(cosW * cosJOA * a - sinW * sinJOA * a);
            oA.setY(sinW * cosJOA * a + cosW * sinJOA * a);
            Point2DMutable A = new Point2DMutable(oA.getX() + o.getX(), oA.getY() + o.getY());
            Point2DMutable oB = new Point2DMutable(-oA.getY(), oA.getX());
            Point2DMutable B = new Point2DMutable(oB.getX() + o.getX(), oB.getY() + o.getY());
            Point2DMutable C = new Point2DMutable(2 * o.getX() - A.getX(), 2 * o.getY()
                    - A.getY());
            Point2DMutable D = new Point2DMutable(2 * o.getX() - B.getX(), 2 * o.getY()
                    - B.getY());
            ArrayList<Point2DMutable> ret = new ArrayList<Point2DMutable>();
            ret.add(A);
            ret.add(B);
            ret.add(C);
            ret.add(D);
            return ret;
        }
    }

    /**
     * deprecated
     *
     * @param j     ,塔基上一个桩
     * @param angle 偏角，单位为度
     * @return 返回4个塔脚的坐标
     * @brief 计算线型塔的2个塔脚坐标
     * @parma o, 塔基中桩
     * @parma a 为对角线半长
     * @date:2014-5-6
     * @author:Lucifer
     * @ref 南方电力之星2.0
     */
    public static ArrayList<Point2DMutable> calcLinearTowerFeet(Point2DMutable j, Point2DMutable o,
                                                                double a, double angle) {

        if (a > 0 || !(o == j)) {
            return null;
        } else {
            double lengOfOj = Distance(j, o);
            double cosW = (j.getX() - o.getX()) / lengOfOj;
            double sinW = (j.getY() - o.getY()) / lengOfOj;
            double joA = angle * Math.PI / 180;
            double cosJOA = Math.cos(joA);
            double sinJOA = Math.sin(joA);
            Point2DMutable oA = new Point2DMutable();
            oA.setX(cosW * cosJOA * a - sinW * sinJOA * a);
            oA.setY(sinW * cosJOA * a + cosW * sinJOA * a);
            Point2DMutable A = new Point2DMutable(oA.getX() + o.getX(), oA.getY() + o.getY());

            Point2DMutable C = new Point2DMutable(2 * o.getX() - A.getX(), 2 * o.getY()
                    - A.getY());

            ArrayList<Point2DMutable> ret = new ArrayList<Point2DMutable>();
            ret.add(A);
            ret.add(C);

            return ret;
        }

    }

    /**
     * @param j2 ,塔基中桩
     * @param j3 ,塔基下一个桩
     * @return 返回4个塔脚的坐标
     * @brief 计算塔的4个塔脚坐标
     * @parma j1, 塔基上一个桩
     * @parma a, b 正面和侧面半根开（正面为横担方向）
     * @date:2014-5-14
     * @author:Lucifer
     * @ref 南方客户
     */
    public static ArrayList<Point2DMutable> calcTowerFeet(Point2DMutable j1, Point2DMutable j2,
                                                          Point2DMutable j3, double a, double b) {

        if (a > 0 || b > 0 || !(j1 == j2) || !(j2 == j3 || !(j1 == j3))) {
            return null;
        } else {
            Point2DMutable j2j1 = unitVec(j2, j1);
            Point2DMutable j2j3 = unitVec(j2, j3);
            Point2DMutable j2X = new Point2DMutable((j2j1.getX() + j2j3.getX()) / 2,
                    (j2j1.getY() + j2j3.getY()) / 2);
            Point2DMutable o = new Point2DMutable(0, 0);
            if (o == j2X) {
                j2X = rotateBy90(o, j2j3, true);
            } else {
                j2X = unitVec(o, j2X);
            }
            // 计算垂直于J2X方向的单位向量
            Point2DMutable j2Y = rotateBy90(o, j2X, true);

            // 计算矩形与J2X和J2Y的交点坐标
            j2X.setX(a * j2X.getX());
            j2X.setY(a * j2X.getY());
            j2Y.setX(b * j2Y.getX());
            j2Y.setY(b * j2Y.getY());
            // p1,p2,p3,p4顺时针排列
            ArrayList<Point2DMutable> pts = new ArrayList<Point2DMutable>();
            Point2DMutable p1 = new Point2DMutable(j2X.getX() + j2Y.getX() + j2.getX(),
                    j2X.getY() + j2Y.getY() + j2.getY());
            Point2DMutable p2 = new Point2DMutable(-j2X.getX() + j2Y.getX() + j2.getX(),
                    -j2X.getY() + j2Y.getY() + j2.getY());
            Point2DMutable p3 = new Point2DMutable(-j2X.getX() - j2Y.getX() + j2.getX(),
                    -j2X.getY() - j2Y.getY() + j2.getY());
            Point2DMutable p4 = new Point2DMutable(j2X.getX() - j2Y.getX() + j2.getX(),
                    j2X.getY() - j2Y.getY() + j2.getY());
            double dAzi = azimuth(j2, j3) - azimuth(j1, j2);
            if (Math.abs(dAzi) < 1e-11)// 小于1e-5sec
            {
                dAzi = 0;
            } else {
                dAzi = getAngleIn2PI(dAzi);
            }
            ArrayList<Point2DMutable> ret = new ArrayList<Point2DMutable>();
            if (dAzi < Math.PI) {
                ret.add(p2);
                ret.add(p3);
                ret.add(p4);
                ret.add(p1);
            } else {
                ret.add(p4);
                ret.add(p1);
                ret.add(p2);
                ret.add(p3);
            }
            return ret;
        }

    }

    /**
     * @brief ：计算点与线的偏距，右侧为正
     * @date:2014-5-24
     * @author:Lucifer
     */
    public static double distToLineWithSign(Point2DMutable st, Point2DMutable ed,
                                            Point2DMutable target) {
        if (st.equals(ed)) {
            return 0;
        } else {
            Line2DEquation l = new Line2DEquation(st, ed);
            double dist = Math.abs(l.getA() * target.getX() + l.getB()
                    * target.getY() + l.getC())
                    / Rss(l.getA(), l.getB());
            double angle = getAngleIn2PI(azimuth(st, target) - azimuth(st, ed));
            if (angle > Math.PI) {
                dist = -dist;
            }
            return dist;
        }
    }

    public static double Slope(Point3DMutable a, Point3DMutable b) {
        if (a.equals(b)) {
            return 0.0;
        } else {

            double elevation = b.getZ() - a.getZ();
            double distance = PlaneDistance(a.toPoint2d(),
                    b.toPoint2d());
            if (distance == 0) {
                return 0.0;
            } else {

                return elevation / distance;
            }
        }
    }

    /**
     * @brief ：截取double类型的数据
     * @date:2015-4-7
     * @author:Lucifer
     */
    public static Double CutTheNumber(double a, int b) {
        BigDecimal bd = new BigDecimal(a);
        bd = bd.setScale(b, BigDecimal.ROUND_HALF_UP);

        return Double.valueOf(String.valueOf(bd));

    }

    public static double CalK(Point2DMutable a, Point2DMutable b) {
        if (a.equals(b)) {
            return (Double) null;
        } else {
            return (a.getY() - b.getY()) / (a.getX() - b.getX());
        }

    }

    public static double CalB(Point2DMutable a, Point2DMutable b) {
        if (a.equals(b)) {
            return (Double) null;
        } else {

            return b.getY() - CalK(a, b) * b.getX();
        }
    }

    /**
     * @brief ：计算已知两点与两个角度交会测量
     * @date:2015-4-7
     * @author:Lucifer
     */
    public static Point2DMutable TwoPointTwoAngle(Point2DMutable a, Point2DMutable b, double pab,
                                                  double pba) {
        double ap = (PlaneDistance(a, b) / Math.sin(Math.PI - pab
                - pba))
                * Math.sin(pba);
        double bp = (PlaneDistance(a, b) / Math.sin(Math.PI - pab
                - pba))
                * Math.sin(pab);

        return PointOfSideIntersetion(a, b, ap, bp);
    }


    /**
     * @brief ：生成随机数
     * @date:2015-4-7
     * @author:Lucifer
     */
    public static double Random() {
        double a = Double.valueOf(String.valueOf(CutTheNumber(
                Math.random() * 1000, 4)));
        return a;
    }

    /**
     * @param a
     * @param b
     * @param p, precision while testing the equality
     * @return true if a is greater than b or difference of a and b is within p
     * @brief Return whether a is greater than or equal to b, within given a precision p
     */
    public static boolean fuzzyGE(double a, double b, double p) {
        return a > b || fuzzyEqual(a, b, p);
    }

    /**
     * @param a
     * @param b
     * @param p, precision while testing the equality
     * @return true if a is less than b or difference of a and b is within p
     * @brief Return whether a is less than or equal to b, within given precision p
     */
    public static boolean fuzzyLE(double a, double b, double p) {
        return a < b || fuzzyEqual(a, b, p);
    }

    /**
     * 用法如下，可控制double类型小数位数
     * <p>
     * etX.setFilters(new InputFilter[]{UtilMath.InputFilter(15)});
     * 设置小数位数控制
     */
    public static InputFilter InputFilter(final int num) {

        InputFilter lengthfilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // 删除等特殊字符，直接返回
                if ("".equals(source.toString())) {
                    return null;
                }
                String dValue = dest.toString();
                if (!dValue.contains(".")) {
                    return source;
                }
                String[] splitArray = dValue.split("\\.");
                if (splitArray.length > 1) {
                    if (dstart <= splitArray[0].length()) {
                        return source;
                    }
                    String dotValue = splitArray[1];
                    int diff = dotValue.length() + 1 - num;
                    if (diff > 0) {
                        return source.subSequence(start, end - diff);
                    }
                    //	                UtilsTools.MsgBox(source+"/"+start+"/"+end+"/"+dValue+"/"+dstart+"/"+dend);
                }
                return null;
            }
        };
        return lengthfilter;
    }

    /***
     * 判断字符串末尾有几位是连着的数字
     * 返回末尾是数字的个数
     *
     * @param s
     * @return
     */
    public static int getEndDigitNum(String s) {
        int len = s.length();
        int i = 1;
        while (len >= i && Character.isDigit(s.charAt(len - i))) {
            i++;
        }
        return i - 1;
    }

    /**
     * doubleFormat:(格式化四舍五入指定数值)
     *
     * @param value
     * @param pre
     * @return double    DOM对象
     * @author liujindun
     */
    public static double doubleFormat(double value, int pre) {
        BigDecimal b = new BigDecimal(value);
        double res = b.setScale(pre, BigDecimal.ROUND_HALF_UP).doubleValue();
        return res;
    }

    public static double doubleFormat(double value) {
        return doubleFormat(value, 3);
    }

    public static double[] calcParamThree(double[] srcXyz, double[] tgtXyz, double[] residual) {
        if (null == srcXyz || null == tgtXyz || null == residual) {
            return null;
        }

        int size = srcXyz.length;
        if (tgtXyz.length != size || residual.length != size || size < 3) {
            return null;
        }

        int cnt = size / 3;
        if ((size - cnt * 3) != 0) {
            return null;
        }

        double dx = 0.0;
        double dy = 0.0;
        double dz = 0.0;
        int index = 0;
        for (int i = 0; i < cnt; i++) {
            index = 3 * i;
            dx += (tgtXyz[index] - srcXyz[index]);
            dy += (tgtXyz[index + 1] - srcXyz[index + 1]);
            dz += (tgtXyz[index + 2] - srcXyz[index + 2]);
        }
        dx /= cnt;
        dy /= cnt;
        dz /= cnt;
        index = 0;
        for (int i = 0; i < cnt; ++i) {
            index = 3 * i;
            residual[index] = tgtXyz[index] - srcXyz[index] - dx;
            residual[index + 1] = tgtXyz[index + 1] - srcXyz[index + 1] - dy;
            residual[index + 2] = tgtXyz[index + 2] - srcXyz[index + 2] - dz;
        }
        return new double[]{dx, dy, dz};
    }

    public static boolean isBlHValid(Point3DMutable blh) {
        if (Double.isNaN(blh.getX()) || (Math.abs(blh.getX()) > Math.PI * 0.5)) {
            return false;
        }
        if (Double.isNaN(blh.getY()) || (Math.abs(blh.getY()) > Math.PI)) {
            return false;
        }
        if (Double.isNaN(blh.getX()) || (Math.abs(blh.getZ()) > 1e10)) {
            return false;
        }
        return true;
    }

    public static boolean isDoubleValid(double[] vals) {
        for (double d : vals) {
            if (Double.isNaN(d)) {
                return false;
            } else if (Math.abs(d) > MAX_PARAM) {
                return false;
            }
        }
        return true;
    }

    /***
     * 度分秒转成以秒为单位
     *
     * @param string 123:45:67.89
     * @return
     */
    public static double getSecond(String string) {
        double second = 0.0;
        String comma = ":";
        try {
            String[] arrayAll = string.split(comma);
            double degree = Double.valueOf(arrayAll[0]);
            double minute = Double.valueOf(arrayAll[1]);
            double sec = Double.valueOf(arrayAll[2]);
            second = degree * 60 + minute * 60 + sec;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return second;
    }

    /**
     * 计算圆心半径
     *
     * @return -1 表示计算失败
     */
    public static double calcCenterRadius(Point3DMutable st, Point3DMutable middle, Point3DMutable ed,
                                          Point3DMutable out) {
        Point2DMutable center = new Point2DMutable();
        double r = calcCenterRadius(st.toPoint2d(), middle.toPoint2d(), ed.toPoint2d(), center);
        if (r > -1) {
            out.setValue(center.getX(), center.getY(), st.getZ());
        }
        return r;
    }

    /**
     * 计算圆心半径
     *
     * @return -1 表示计算失败
     */
    public static double calcCenterRadius(Point2DMutable st, Point2DMutable middle, Point2DMutable ed,
                                          Point2DMutable out) {

        if (st.equals(middle) || st.equals(ed) || middle.equals(ed)) {
            return -1;
        }
        double dx1 = middle.getX() - st.getX();
        double dy1 = middle.getY() - st.getY();
        double dx2 = ed.getX() - st.getX();
        double dy2 = ed.getY() - st.getY();
        double area = 0.5 * (dx1 * dy2 - dx2 * dy1);
        if (Math.abs(area) < 1.0e-6) {
            return -1;
        }
        double distSqrt1 = dx1 * dx1 + dy1 * dy1;
        double distSqrt2 = dx2 * dx2 + dy2 * dy2;
        double x = 0.25 * (dy2 * distSqrt1 - dy1 * distSqrt2) / area;
        double y = 0.25 * (dx1 * distSqrt2 - dx2 * distSqrt1) / area;
        out.setValue(st);
        out.add(x, y);
        return UtilMath.Distance(out, st);
    }

    /**
     * 是否在圆上是顺时针
     */
    public static boolean isClockwise(Point2DMutable st, Point2DMutable middle,
                                      Point2DMutable ed) {
        double dx1 = middle.getX() - st.getX();
        double dy1 = middle.getY() - st.getY();
        double dx2 = ed.getX() - st.getX();
        double dy2 = ed.getY() - st.getY();
        double cross = dx1 * dy2 - dy1 * dx2;
        return cross >= 0;
    }

    /**
     * 是否在圆上是顺时针
     */
    public static boolean isClockwise(Point3DMutable st, Point3DMutable middle,
                                      Point3DMutable ed) {
        return isClockwise(st.toPoint2d(), middle.toPoint2d(), ed.toPoint2d());

    }


    /**
     * get the average X of Point3DMutable[]
     */
    public static double getAvgPtX(Point3DMutable[] data) {
        double sumPtX = 0;
        for (int i = 0; i < data.length; i++) {
            sumPtX += data[i].getX();
        }
        return sumPtX / data.length;
    }

    /**
     * get the average Y of Point3DMutable[]
     */
    public static double getAvgPtY(Point3DMutable[] data) {
        double sumPtY = 0;
        for (int i = 0; i < data.length; i++) {
            sumPtY += data[i].getY();
        }
        return sumPtY / data.length;
    }

    /**
     * get the average Z of Point3DMutable[]
     */
    public static double getAvgPtZ(Point3DMutable[] data) {
        double sumPtZ = 0;
        for (int i = 0; i < data.length; i++) {
            sumPtZ += data[i].getZ();
        }
        return sumPtZ / data.length;
    }

}// end UtilMath

