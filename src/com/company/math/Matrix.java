package com.company.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Created by jarma on 7/2/2017.
 */
public class Matrix <T>{
    private List<List<T>> values;//rows

    private Matrix(int v, int h, Supplier<T> supplier){
        values = new ArrayList<>();
        for (int i = 0; i < v; i++) {
            values.add(i, new ArrayList<>());
            for (int j = 0; j < h; j++)
                values.get(i).add(j, supplier.get());
        }
    }

    public static <X> Matrix<X> of(int v, int h, Supplier<X> supplier){
        return new Matrix<>(v, h, supplier);
    }

    public static <X> Matrix<X> of(int v, int h, X element){
        return new Matrix<>(v, h, () -> element);
    }

    public static Matrix<Double> ofGaussian(int v, int h) {
        return new Matrix<>(v, h, new Random()::nextGaussian);
    }

    public static List<Matrix<Double>> listOfGaussianMatrix(List<Integer> vs, List<Integer> hs){
        if(vs.size() != hs.size())
            throw new IllegalArgumentException("vs and hs must be of the same size");
        List<Matrix<Double>> list = new ArrayList<>(vs.size());
        for (int i = 0; i < vs.size(); i++) {
            list.add(i, Matrix.ofGaussian(vs.get(i), hs.get(i)));
        }
        return list;
    }

    public static List<Matrix<Double>> listOfGaussianMatrix(Integer v, List<Integer> hs){
        List<Matrix<Double>> list = new ArrayList<>(hs.size());
        for (int i = 0; i < hs.size(); i++) {
            list.add(i, Matrix.ofGaussian(v, hs.get(i)));
        }
        return list;
    }

    public static List<Matrix<Double>> listOfGaussianMatrix(List<Integer> vs, Integer h){
        List<Matrix<Double>> list = new ArrayList<>(vs.size());
        for (int i = 0; i < vs.size(); i++) {
            list.add(i, Matrix.ofGaussian(vs.get(i), h));
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

    public static void main(String[] args) {
        List<Integer> list1 = Arrays.asList(2,1,5);
        List<Integer> list2 = Arrays.asList(2,2,2);
        String s0 = Matrix.listOfGaussianMatrix(list1, 1).toString();
        String s1 = Matrix.listOfGaussianMatrix(list1, list2).toString();
        System.out.println(s1);
    }

    public T get(int a, int b){
        return values.get(a).get(b);
    }

    public List<T> getRow(int a){
        return values.get(a);
    }

    public List<T> getColumn(int b){
        List<T> column = new ArrayList<>();
        for (List<T> rows: values) {
            column.add(rows.get(b));
        }
        return column;
    }

    public int size(){
        return values.size();
    }
}
