package com.company.neuron;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
/**
 * Created by jarma on 7/1/2017.
 */
public class NeuronalNetwork {
    /*
    def __init__(self, sizes):
        self.num_layers = len(sizes)
        self.sizes = sizes
        self.biases = [np.random.randn(y, 1) for y in sizes[1:]]
        self.weights = [np.random.randn(y, x)
                        for x, y in zip(sizes[:-1], sizes[1:])]
     */
    static Random random = new Random();

    Integer [] layerSizes;
    List<List<Double>> biases;//matrix
    List<List<Double>> weights;//matrix

    public NeuronalNetwork(Integer ... layerSizes) {
        this.layerSizes = layerSizes;
        List<Integer> biasesSizes = new ArrayList(Arrays.asList(layerSizes));
        biasesSizes.remove(0);
        List<Integer> weightSizes = new ArrayList(Arrays.asList(layerSizes));
        weightSizes.remove(weightSizes.size() - 1);
        biases = generate(biasesSizes);
        weights = generate(weightSizes);
    }

    private List<List<Double>> generate(List<Integer> layerSizes) {
        List<List<Double>> generated = new ArrayList<>();
        for (int i = 0; i < layerSizes.size(); i++) {
            int currentLayerLength = layerSizes.get(i);
            List<Double> currentLayer = new ArrayList<>();
            for (int j = 0; j < currentLayerLength; j++) {
                currentLayer.add(j, random.nextDouble());
            }
            generated.add(currentLayer);
        }
        return generated;
    }

    public static void main(String[] args) {
        //zip(Arrays.asList(1.0, 2.0, 3.0), Arrays.asList(1.0, 4.0, 9.0));
        new NeuronalNetwork(2 , 2, 2);
    }

    public static List<List<Double>> zip(List<Double> ... lists){
        List<List<Double>> zList = new ArrayList<>();
        int numberOfLists = lists.length;
        int lengthOfEachLists = lists[0].size();
        lists[0].forEach(e -> zList.add(new ArrayList<>()));
        for (int i = 0; i < lengthOfEachLists; i++) {
            for (int j = 0; j < numberOfLists; j++) {
                zList.get(i).add(j, lists[j].get(i));
            }
        }
        System.out.println(zList);
        return zList;
    }

}
