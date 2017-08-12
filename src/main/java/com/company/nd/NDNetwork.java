package com.company.nd;

import static org.nd4j.linalg.ops.transforms.Transforms.abs;
import static org.nd4j.linalg.ops.transforms.Transforms.pow;
import static org.nd4j.linalg.ops.transforms.Transforms.sqrt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Created by jarma on 8/8/2017.
 */
public class NDNetwork {

  List<NDLayer> layers;

  public NDNetwork(int... inputSizeAndlayersSizes) {
    this(null, inputSizeAndlayersSizes);
  }

  public NDNetwork(String optionalActivationFun, int... inputSizeAndlayersSizes) {
    layers = new ArrayList<>();
    for (int i = 1; i < inputSizeAndlayersSizes.length; i++) {
      int numberOfWeightsPerNeuron = inputSizeAndlayersSizes[i - 1];
      int numberOfNeurons = inputSizeAndlayersSizes[i];
      layers.add(new NDLayer(i - 1, numberOfWeightsPerNeuron, numberOfNeurons));
      layers.get(i - 1).setActivationName(optionalActivationFun);
    }
  }

  public INDArray feedforward(Float... inputs) {
    float[] ff = new float[inputs.length];
    for (int i = 0; i < ff.length; i++) {
      ff[i] = inputs[i];
    }
    INDArray x = Nd4j.create(ff);
    return feedforward(x);
  }

  public INDArray feedforward(INDArray x) {
    for (int i = 0; i < layers.size(); i++) {
      x = layers.get(i).activate(x);
    }
    return x;
  }

  public float error(INDArray in, INDArray expectedOut) {
    INDArray out = feedforward(in);
    return abs(expectedOut.sub(out)).getFloat(0);
  }

  @Override
  public String toString() {
    List<List<String>> ls = layers
        .stream()
        .map(l -> l.toStringShortest().split("\n"))
        .map(Arrays::asList)
        .collect(Collectors.toList());
    return "Network:\n" + ls.toString()
        .replaceAll("\\[L", "\n\\[L")
        .replaceAll("]]", "]\n]");
  }

  public String toStringShort() {
    List<List<String>> ls = layers
        .stream()
        .map(l -> l.toStringMini().split("\n"))
        .map(Arrays::asList)
        .collect(Collectors.toList());
    return "Network:\n" + ls.toString()
        .replaceAll("\\[L", "\n\\[L")
        .replaceAll("]]", "]\n]");
  }

  public static void main(String[] args) {//x -> w + b = y
    NDNetwork net = new NDNetwork(2, 2, 2);
//    INDArray inputs = Nd4j.create(new float[]{1f, 1f}, new int[]{2,1});
//    INDArray x = net.feedforward(inputs);
    float y = net.error(
        Nd4j.create(new float[]{1f, 1f}, new int[]{2,1}),//input
        Nd4j.create(new float[]{1f, 1f}, new int[]{2,1}));//output
    System.out.println(y);

    net.layers.forEach(l -> l.setActivationName("relu"));
    y = net.error(
        Nd4j.create(new float[]{1f, 1f}, new int[]{2,1}),//input
        Nd4j.create(new float[]{1f, 1f}, new int[]{2,1}));//output
    System.out.println(y);

    net.layers.forEach(l -> l.setActivationName("sigmoid"));
    y = net.error(
        Nd4j.create(new float[]{1f, 1f}, new int[]{2,1}),//input
        Nd4j.create(new float[]{1f, 1f}, new int[]{2,1}));//output
    System.out.println(y);
  }
}