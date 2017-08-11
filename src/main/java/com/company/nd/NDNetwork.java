package com.company.nd;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * Created by jarma on 8/8/2017.
 */
public class NDNetwork {
  List<NDLayer> layers;

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
        .map(l -> l.toStringShortest().split("\n"))
        .map(Arrays::asList)
        .collect(Collectors.toList());
    return "Network:\n" + ls.toString()
        .replaceAll("\\[L", "\n\\[L")
        .replaceAll("]]", "]\n]");
  }

  public NDNetwork(int ... inputSizeAndlayersSizes) {
    layers = NDLayer.newfullyConnected(inputSizeAndlayersSizes);
  }

  public static void main(String[] args) {
    NDNetwork net = new NDNetwork(2, 2, 2);
    System.out.println(net);
  }

  private List<INDArray> feedforward(List<INDArray> x) {
    for (int i = 0; i < layers.size(); i++) {
      x = layers.get(i).activate(x);
    }
    return x;
  }
}
