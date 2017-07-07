package com.company.neuron;

import com.company.math.Matrix;

import java.util.*;
import java.util.function.Function;
import java.util.stream.DoubleStream;

/**
 * based on pythons perceptron http://neuralnetworksanddeeplearning.com/chap1.html
 *
 * @Author Armando Cordova
 */
public class MatrixNN {
    public final int numberOfInputs;//seed and info
    public final List<Integer> layerSizes;//seed and info
    private final List<Matrix> biases;//data
    private final List<Matrix> weights;//data

    public MatrixNN(Integer... inputSizeAndlayersSizes) {
        List<Integer> noFirst = new ArrayList(Arrays.asList(inputSizeAndlayersSizes));
        noFirst.remove(0);
        List<Integer> noLast = new ArrayList(Arrays.asList(inputSizeAndlayersSizes));
        noLast.remove(noLast.size() - 1);
        this.biases = Matrix.listOfGaussianMatrix(noFirst, 1);
        this.weights = Matrix.listOfGaussianMatrix(noFirst, noLast);
        this.numberOfInputs = inputSizeAndlayersSizes[0];
        this.layerSizes = noFirst;
    }

    private Matrix feedforward(Matrix a) {
        for (int i = 0; i < weights.size(); i++) {
            Matrix w = weights.get(i);
            Matrix b = biases.get(i);
            a = Matrix.dot(w, a).add(b).sigmoid();
        }
        return a;
    }

    public static void main(String[] args) throws Exception {
        //generate Network
        MatrixNN net = new MatrixNN(1, 2);
        //identity training data
        Map<Matrix, Matrix> trainingData = new HashMap<>();
        Matrix input = null;
        Matrix output = null;
        for (int i = 0; i < 100; i++) {
            input = Matrix.ofGaussian(1, 1);
            List<List<Double>> outputValues = new ArrayList<>();
            outputValues.add(Arrays.asList(input.getRow(0).get(0)));
            outputValues.add(Arrays.asList(input.getRow(0).get(0)));
            output = new Matrix(outputValues);
            trainingData.put(input, output);
        }
        //error
        System.out.print("expected");
        System.out.println(trainingData.get(input));//expected
        System.out.print("output");
        System.out.println(net.feedforward(input));//output
        System.out.print("output");
        System.out.println(net.feedforward(input));//output
        System.out.print("output");
        System.out.println(net.feedforward(input));//output
        System.out.print("substract");
        System.out.println(output.substract(net.feedforward(input)));//output
        net.error(input, output);//output

    }

    //loss or cost
    private Matrix error(Matrix input, Matrix expectedOutput) {
        Matrix errorM;
        double error = 0;
        double error2 = 0;
        int correct = 0;
        Matrix result = feedforward(input);
        Matrix diference = expectedOutput.substract(result);
        /*error += diference.stream()
                .flatMap(e -> e.stream())
                .mapToDouble(e -> Math.pow(e, 2))
                .sum() / diference.size();
        error2 += diference.stream()
                .flatMap(e -> e.stream())
                .mapToDouble(e -> Math.abs(e))
                .sum() / diference.size();
        correct += diference.stream()
                .flatMap(e -> e.stream())
                .filter(e -> (e > -0.2 && e < 0.2))
                .mapToInt(e -> 1)
                .sum();*/
//        System.out.println("error   "+error);
//        System.out.println("error2  "+error2);
//        System.out.println("correct "+correct);
        System.out.println(diference);
        System.out.println(expectedOutput);
        return diference;
}
}