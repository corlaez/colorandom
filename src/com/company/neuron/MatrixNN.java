package com.company.neuron;

import com.company.math.Matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * based on pythons perceptron
 *
 def __init__(self, sizes):
 self.num_layers = len(sizes)
 self.sizes = sizes
 self.biases = [np.random.randn(y, 1) for y in sizes[1:]]
 self.weights = [np.random.randn(y, x)
 for x, y in zip(sizes[:-1], sizes[1:])] *
 * Created by jarma on 7/1/2017.
 */
public class MatrixNN {
    static Random random = new Random();

    public Integer[] layerSizes;

    private List<Matrix<Double>> biases;
    private List<Matrix<Double>> weights;

    public MatrixNN(Integer... layerSizes) {
        this.layerSizes = layerSizes;
        List<Integer> biasesSizes = new ArrayList(Arrays.asList(layerSizes));
        biasesSizes.remove(0);
        List<Integer> weightSizes = new ArrayList(Arrays.asList(layerSizes));
        weightSizes.remove(weightSizes.size() - 1);
        biases = Matrix.listOfGaussianMatrix(biasesSizes, 1);
        weights = Matrix.listOfGaussianMatrix(weightSizes, biasesSizes);
        System.out.println("biases " + biases.size() + " layer" + (biases.size() > 1 ? "s" : ""));
        System.out.println(biases);
        System.out.println("weights " + weights.size() + " layer" + (weights.size() > 1 ? "s" : ""));
        System.out.println(weights);
    }

    public static void main(String[] args) throws Exception {
        MatrixNN network = new MatrixNN(1, 3, 2);
        Thread.sleep(1000);
        //double bias00 = network.getPerceptronBias(1,1);
        //System.out.println(bias00);
        List<Double> weights00 = network.getPerceptronWeights(1,1);
        System.out.println(weights00);
    }

    private Double getPerceptronBias(int layer, int row){
        Matrix<Double> biasesOfLayer = getLayer(biases, layer);
        return biasesOfLayer.get(row, 0);//always one bias per perceptron
    }

    private List<Double> getPerceptronWeights(int layer, int row){
        Matrix<Double> weightsOfLayer = getLayer(weights, layer);
        return weightsOfLayer.getColumn(row);//column or row???
    }

    private <T> Matrix<T> getLayer(List<Matrix<T>> list, int index){
        Matrix<T> biasesOfLayer;
        try {
            biasesOfLayer = list.get(index);
        } catch (Exception e){
            throw new IllegalArgumentException("Invalid layer index. Layers size: " + biases.size());
        }
        return biasesOfLayer;
    }

    class Perceptron {
        int layer;
        int row;
        public Perceptron(int layer, int row){
            this.layer = layer;
            this.row = row;
        }
        public double getBias(){
            return getPerceptronBias(layer, row);
        }
        public List<Double> getWeigths(){
            return getPerceptronWeights(layer, row);
        }
        @Override
        public String toString() {
            return "Perceptron("+ layer +", " + row + "{" +
                    "bias= " + getBias() +
                    ", weights= " + getWeigths() +
                    '}';
        }
    }
}
