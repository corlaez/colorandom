package com.company.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by jarma on 7/2/2017.
 */
public class Matrix{
    public List<List<Double>> values;//rows

    private Matrix(int rows, int columns, Supplier<Double> supplier){
        values = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            values.add(i, new ArrayList<>());
            for (int j = 0; j < columns; j++)
                values.get(i).add(j, supplier.get());
        }
    }

    public Matrix(List<List<Double>> values){
        this.values = values;
    }

    public static Matrix of(int h, int v, Supplier<Double> supplier){
        return new Matrix(h, v, supplier);
    }

    public static Matrix of(int h, int v, Double d){
        return new Matrix(h, v, () -> d);
    }

    public static Matrix ofGaussian(int h, int v) {
        return new Matrix(h, v, new Random()::nextGaussian);
    }

    public static List<Matrix> listOfGaussianMatrix(List<Integer> hss, List<Integer> vs){
        if(hss.size() != vs.size())
            throw new IllegalArgumentException("vs and hs must be of the same size");
        List<Matrix> list = new ArrayList<>(hss.size());
        for (int i = 0; i < hss.size(); i++) {
            list.add(i, Matrix.ofGaussian(hss.get(i), vs.get(i)));
        }
        return list;
    }

    public static List<Matrix> listOfGaussianMatrix(Integer h, List<Integer> vs){
        List<Matrix> list = new ArrayList<>(vs.size());
        for (int i = 0; i < vs.size(); i++) {
            list.add(i, Matrix.ofGaussian(h, vs.get(i)));
        }
        return list;
    }

    public static List<Matrix> listOfGaussianMatrix(List<Integer> hs, Integer v){
        List<Matrix> list = new ArrayList<>(hs.size());
        for (int i = 0; i < hs.size(); i++) {
            list.add(i, Matrix.ofGaussian(hs.get(i), v));
        }
        return list;
    }

    @Override
    public String toString() {
        String s =values.stream()
                .map(v -> v.toString())
                .reduce((a, b) -> a + "\n" + b)
                .get();
        return "\nMatrix(" + values.size() + " * " + values.get(0).size() + "):\n[" + s + "]\n";
    }

    public List<Double> getRow(int a){
        return values.get(a);
    }

    public static Matrix dot(Matrix a, Matrix b) {
        if (a.getRow(0).size() != b.size())
            throw new RuntimeException("not possible: " + a.getRow(0).size() + " != " + b.size());
        //prepare answer
        List<List<Double>> r = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            r.add(new ArrayList<>());
            for (int j = 0; j < b.getRow(0).size(); j++) {
                r.get(i).add(0.0);
            }
        }
        //operate
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.getRow(0).size(); j++) {
                for (int k = 0; k < a.getRow(0).size(); k++) {
                    r.get(i).set(j,r.get(i).get(j) +
                            a.getRow(i).get(k) * b.getRow(k).get(j));
                }
            }
        }
        return new Matrix(r);
    }

    public Matrix sigmoid(){
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < getRow(0).size(); j++) {
                double sigmoid = 1.0 / (1.0 + Math.exp(values.get(i).get(j)));
                values.get(i).set(j, sigmoid);
            }
        }
        return this;
    }

    public int size(){
        return values.size();
    }

    public Matrix add(Matrix b) {
        if(b.size() != size()) throw new RuntimeException("impossible. Not equal size " + size() + " != " + b.size());
        Matrix copy = copy();
        for (int i = 0; i < copy.size(); i++) {
            for (int j = 0; j < copy.getRow(0).size(); j++) {
                double newVal = copy.getRow(i).get(j) + b.getRow(i).get(0);
                copy.values.get(i).set(j, newVal);
            }
        }
        return copy;
    }

    public Matrix substract(Matrix b) {
        if(b.size() != size()) throw new RuntimeException("impossible. Not equal size " + size() + " != " + b.size());
        Matrix copy = copy();
        for (int i = 0; i < copy.size(); i++) {
            for (int j = 0; j < copy.getRow(0).size(); j++) {
                double newVal = copy.getRow(i).get(j) - b.getRow(i).get(0);
                copy.values.get(i).set(j, newVal);
            }
        }
        return copy;
    }

    public Matrix copy() {
        List<List<Double>> list = new ArrayList<>();
        for (List<Double> value: values) {
            List<Double> doubleList = new ArrayList<>();
            doubleList.addAll(value);
            list.add(doubleList);
        }
        return new Matrix(list);
    }

/*
    public List<Double> getColumn(int b){
        List<Double> column = new ArrayList<>();
        for (List<Double> rows: values) {
            column.add(rows.get(b));
        }
        return column;
    }
*/

    public Stream<List<Double>> stream(){
        return values.stream();
    }

    public static void main(String[] args) {
        List<List<Double>> m1 = new ArrayList<>();
        m1.add(Arrays.asList(1.0, 2.0, 3.0));
        m1.add(Arrays.asList(8.0, 9.0, 10.0));
        List<List<Double>> m2 = new ArrayList<>();
        m2.add(Arrays.asList(1.0, 2.0, 3.0, 10.0));
        m2.add(Arrays.asList(4.0, 6.0, 5.0, 6.0));
        m2.add(Arrays.asList(8.0, 9.0, 10.0, 1.0 ));
        System.out.println(Matrix.dot(new Matrix(m1), new Matrix(m2)));
        m1 = new ArrayList<>();
        m1.add(Arrays.asList(1d, 3d));
        m1.add(Arrays.asList(2d, 4d));
        m2 = new ArrayList<>();
        m2.add(Arrays.asList(1.0, 2.0, 3.0, 10.0));
        m2.add(Arrays.asList(1.0, 2.0, 3.0, 10.0));
        List<List<Double>> m3 = new ArrayList<>();
        m3.add(Arrays.asList(1d));
        m3.add(Arrays.asList(2d));
        System.out.println(Matrix.dot(new Matrix(m1), new Matrix(m2)).add(new Matrix(m3)));
    }
}
