package com.company.math;

/**
 * Created by jarma on 6/29/2017.
 */
public class ErrorFunction {
    private double m;
    private double b;

    public ErrorFunction(double m, double b){
        this.m = m;
        this.b = b;
    }

    public double getY(double x, double y){
        return m * x + b;
    }
}
