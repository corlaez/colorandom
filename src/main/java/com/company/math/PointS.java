package com.company.math;

import java.util.Objects;

/**
 * Created by jarma on 6/29/2017.
 */
public class PointS {
    public final Double x;
    public final Double y;
    public final int ix;
    public final int iy;

    public PointS(double x, double y){
        this.x = x;
        this.y = y;
        this.ix = roundCheck(this.x);
        this.iy = roundCheck(this.y);
    }

    public static int roundCheck(double origin){
        int round = (int) Math.round(origin);
        if(Math.abs(origin - round) >= 1)
            throw new RuntimeException("Sorry maximum double should not round higher " +
                    "than the highest Integer or lower than the lowest Integer");
        return round;
    }

    @Override
    public String toString() {
        return "PointS{" +
                "x=" + x +
                ", y=" + y +
                ", ix=" + ix +
                ", iy=" + iy +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointS pointS = (PointS) o;
        return Objects.equals(x, pointS.x) &&
                Objects.equals(y, pointS.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
