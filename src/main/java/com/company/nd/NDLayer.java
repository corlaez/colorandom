package com.company.nd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Created by jarma on 8/9/2017.
 */
public class NDLayer {
  int index = -1;
  List<INDArray> weights;//should i use neurons here?
  List<INDArray> biases;

  public NDLayer(int numberOfWeightsPerNeuron, int numberOfNeurons){
    weights = new ArrayList<>();
    biases = new ArrayList<>();
    for (int i = 0; i < numberOfNeurons; i++) {
      weights.add(Nd4j.rand(new int[] {numberOfWeightsPerNeuron, 1}));
      biases.add(Nd4j.rand(new int[] {1, 1}));
    }
    if(weights.size() != biases.size()){
      throw new RuntimeException("W != B");
    }
  }

  public NDLayer(int index, int numberOfWeightsPerNeuron, int numberOfNeurons){
    this(numberOfWeightsPerNeuron, numberOfNeurons);
    this.index = index;
  }

  public static List<NDLayer> newfullyConnected(int ... inputSizeAndlayersSizes){
    List<NDLayer> list = new ArrayList<>();
    for (int i = 1; i < inputSizeAndlayersSizes.length; i++) {
      int numberOfWeightsPerNeuron = inputSizeAndlayersSizes[i-1];
      int numberOfNeurons = inputSizeAndlayersSizes[i];
      list.add(new NDLayer(i-1, numberOfWeightsPerNeuron, numberOfNeurons));
    }
    return list;
  }

  public List<INDArray> activate(List<INDArray> input){
    System.out.println(input);
    System.out.println(this);
    List<INDArray> output = new ArrayList<>();
    for (int i = 0; i < weights.size(); i++) {
      INDArray w = weights.get(i);
      INDArray b = biases.get(i);
      INDArray r = w.mmul(input.get(i))
          .add(b);
      output.add(r);
    }
    return input;
  }

  public int size() {
    return biases.size();
  }

  @Override
  public String toString() {
    String s = "Layer"+ (index > -1 ? " " +index : "") +":\n";
    for (int i = 0; i < weights.size(); i++) {
      s += "Neuron " + i + ": Weights " + weights.get(i) + ", bias " + biases.get(i).toString() + "\n";
    }
    return s;
  }

  public String toStringShort() {
    String s = "Layer"+ (index > -1 ? " " +index : "") +":\n";
    for (int i = 0; i < weights.size(); i++) {
      s += "N" + i + "(" + weights.get(i).length() + ", " + biases.get(i).toString() + ")\n";
    }
    return s;
  }

  public String toStringShortest() {
    String s = "Layer"+ (index > -1 ? " " +index : "") +":\n";
    for (int i = 0; i < weights.size(); i++) {
      s += "N" + i + "(" + weights.get(i).length() + ")\n";
    }
    return s;
  }

  public static void main(String[] args) {
    NDLayer layer = new NDLayer(2,5);
//    System.out.println(layer);
//    System.out.println(layer.toStringShort());
//    System.out.println(layer.toStringShortest());
    System.out.println(layer.activate(Arrays.asList(Nd4j.scalar(0.1),Nd4j.scalar(0.8))));
  }
}
