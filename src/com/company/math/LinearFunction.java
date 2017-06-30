package com.company.math;

import java.util.Collection;
import java.util.Objects;

/**
 * Created by jarma on 6/29/2017.
 */
public class LinearFunction {
    private double m;
    private double b;

    public LinearFunction(double m, double b) {
        this.m = m;
        this.b = b;
    }

    public double getM() {
        return m;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setM(double m) {
        this.m = m;
    }

    public double getY(double x) {
        int ix = PointS.roundCheck(x);
        double dif = ((m * x + b) - (m * ix + b));
        if (dif > 0)
            System.out.println("Diference between getY(x) and getY(xRounded): " + dif);
        return m * x + b;
    }

    public int getIntY(double x) {
        double y = getY(x);
//        try {
        return PointS.roundCheck(y);
//        } catch (Exception e) {
//            int iy = y > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
//            System.out.println("Replacing overflowed " + y + " to " + iy);
//            return iy;
//        }
    }

    public void linearRegression(Collection<PointS> points) {
        boolean out = false;
        double mGradient = 0;//how much the dependent variable is increasing in ms this direction
        double bGradient = 0;//how much the dependent variable is increasing in bs this direction
//        while (!out) {
//            try {
//                mGradient = getErrorMsGradient(points);
//                out = true;
//            } catch (Exception e) {
//                adjustGamma(true);
//            }
//        }
//        out = false;
//        while (!out) {
//            try {
//                bGradient = getErrorBsGradient(points);
//                out = true;
//            } catch (Exception e) {
//                adjustGamma(false);
//            }
//        }
        mGradient = getErrorMsGradient(points);
        bGradient = getErrorBsGradient(points);
        double mStep = mGamma * mGradient;//0.000008
        double bStep = bGamma * bGradient;//0.5
        double m = getM() - mStep;//minus because we want to minimize
        double b = getB() - bStep;//minus because we want to minimize
        setM(m);
        setB(b);
    }

    public double getError(Collection<PointS> points) {
        if (points.isEmpty()) return 0;
        return points.stream()
                .mapToDouble(p -> this.errorOnPoint(p.x, p.y))
                .sum() / points.size();
    }

    private double errorOnPoint(Double x2, Double y2) {
        return Math.pow(y2 - getY(x2), 2);
    }

    private double getErrorMsGradient(Collection<PointS> points) {
        if (points.isEmpty()) return 0;
        return 2 * points.stream()
                .filter(Objects::nonNull)
                .mapToDouble(p -> getHalfErrorMsPartialD(p.x, p.y))
                .sum() / points.size();
    }

    private double getErrorBsGradient(Collection<PointS> points) {
        if (points.isEmpty()) return 0;
        return 2 * points.stream()
                .filter(Objects::nonNull)
                .mapToDouble(p -> getHalfErrorBsPartialD(p.x, p.y))
                .sum() / points.size();
    }

    private Double getHalfErrorMsPartialD(Double x2, Double y2) {
        Double y = getY(x2);
        if (y.isInfinite() || y.isNaN()) return null;
        //System.out.println("-2 * + " + x2 + " * (" + y2 + " - " + y + ")");
        return -1 * x2 * (y2 - y);//half
    }

    private Double getHalfErrorBsPartialD(Double x2, Double y2) {
        Double y = getY(x2);
        if (y.isInfinite() || y.isNaN()) return null;
        //System.out.println("-2 * (" + y2 + " - " + y + ")");
        return -1 * (y2 - getY(x2));//half
    }

    public void adjustGamma(Boolean trueMfalseBnullRandom) {
        if(estatico)
            return;
        if (trueMfalseBnullRandom != null) {
            if(trueMfalseBnullRandom) mGamma /= 10d;
            else bGamma /= 10d;
        }
        else {
            if (gturn % 100000 != 0)  {
                mGamma /= 10d;
                gturn = 0;
            }
            else bGamma /= 10d;
            gturn++;
        }
        b = 1;
        m = 1;
        System.out.println("amGamma: " + mGamma + "abGamma: " + bGamma + " flag: " + trueMfalseBnullRandom);
    }

    int gturn = 0;
    boolean estatico = false;
    double mGamma = 8;//8;
    double bGamma = 5;//500_000;
}