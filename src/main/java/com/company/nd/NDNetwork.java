package com.company.nd;

import static org.nd4j.linalg.ops.transforms.Transforms.pow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    if (inputSizeAndlayersSizes.length < 2) {
      throw new IllegalArgumentException("enter at least input size and a layer size");
    }
    NDLayer[] layers = new NDLayer[inputSizeAndlayersSizes.length - 1];
    for (int i = inputSizeAndlayersSizes.length - 2; i >= 0; i--) {
      int numberOfWeightsPerNeuron = inputSizeAndlayersSizes[i];
      int numberOfNeurons = inputSizeAndlayersSizes[i + 1];
      NDLayer layer = new NDLayer(i, numberOfWeightsPerNeuron, numberOfNeurons);
      if (optionalActivationFun != null) {
        layer.setActivationName(optionalActivationFun);
      }
      layers[i] = layer;
    }
    //output layer activation
    layers[layers.length - 1].setActivationName("identity");
    this.layers = Arrays.asList(layers);
  }

  public INDArray feedforward(INDArray x) {
    for (int i = 0; i < layers.size(); i++) {
      x = layers.get(i).activate(x);
    }
    return x;
  }

  public void UVSGD(Map<INDArray, INDArray> trainMap, int n, int groupSize, float eta, float etaT) {
    List keys = new ArrayList(trainMap.keySet());
    float numberOfGroups = (float) Math.ceil(trainMap.size() * 1.0d / groupSize);
    float etaDelta = (eta - etaT) / (n - 1);
    for (int i = 0; i < n; i++) {//n = epochs, the number of times we train from the map
      Collections.shuffle(keys);//STOCHASTIC
//      System.out.println("Epoch: " + i + "/" + n);
      for (int j = 0; numberOfGroups > j; j++) {
        boolean overflowed = groupSize * (j + 1) > keys.size();
        int sizeOfGroup = overflowed ? keys.size() % groupSize : groupSize;
        int init = j * groupSize;
        int end = init + sizeOfGroup;
        List<INDArray> keyStochasticGroup = keys.subList(init, end);//group = miniBatch
        calculateNablas(trainMap, keyStochasticGroup);
        for (NDLayer l : layers) {
          l.updateLayer(eta, keyStochasticGroup.size());
        }
      }
      eta -= etaDelta;
    }
  }

  private void calculateNablas(Map<INDArray, INDArray> trainMap, List<INDArray> keys) {
    //get gradient from this group of keys
    for (INDArray x : keys) {
      //feedfoward
      INDArray y = feedforward(
          x);//is this redundant in my architecture? Delta could do this but should it?
      //backward pass
      //Get delta from last layer. This is the only place delta makes sense to use.
      NDLayer lastLayer = layers.get(layers.size() - 1);
      INDArray delta = lastLayer.delta(y, trainMap.get(x));
      INDArray secondlastActivation;
      if (layers.size() - 2 >= 0) {
        secondlastActivation = layers.get(layers.size() - 2).activation;
      } else {
        secondlastActivation = x;
      }
      lastLayer.biasesNabla.addi(delta);
      lastLayer.weightsNabla.addi(delta.mmul(secondlastActivation.transpose()));
      for (int i = layers.size() - 2; i >= 0; i--) {
        INDArray prevActivation;
        if (i > 0) {
          prevActivation = layers.get(i - 1).activation;
        } else {
          prevActivation = x;
        }
        NDLayer currL = layers.get(i);
        NDLayer nextL = layers.get(i + 1);
        INDArray sp = currL.activationFuncDerivative.apply(currL.netInput);
        delta = nextL.weights.transpose().mmul(delta).mul(sp);
        currL.weightsNabla.addi(delta.mmul(prevActivation.transpose()));
        currL.biasesNabla.addi(delta);
      }
    }
  }

  public String toStringMini() {
    List<List<String>> ls = layers
        .stream()
        .map(l -> l.toStringMini().split("\n"))
        .map(Arrays::asList)
        .collect(Collectors.toList());
    return "Network:\n" + ls.toString()
        .replaceAll("\\[L", "\n\\[L")
        .replaceAll("]]", "]\n]");
  }

  @Override
  public String toString() {
    List<List<String>> ls = layers
        .stream()
        .map(l -> l.toString().split("\n"))
        .map(Arrays::asList)
        .collect(Collectors.toList());
    return "Network:\n" + ls.toString()
        .replaceAll("\\[L", "\n\\[L")
        .replaceAll("]]", "]\n]");
  }

  public static void main(String[] args) {//x -> w + b = y
    NDLayer.DEFAULT_ACTIVATION = "leakyrelu";
    NDNetwork net = new NDNetwork(784, 784);
    Map<INDArray, INDArray> trainMap = new HashMap<>();
    if (true)//GLOBAL DEBUG
    {
      Nd4j.getRandom().setSeed(123l);
    }
    for (int i = 0; i < 100; i++) {
      INDArray x = Nd4j.rand(784, 1);
      trainMap.put(x, x);
    }
    INDArray x = trainMap.keySet().stream().findAny().get();
    System.out.println(net.toString());
    System.out.println("inp " + x);
    System.out.println("out " + net.feedforward(x));
    System.out.println("err " + net.error(trainMap));

    net.UVSGD(trainMap, 3, 50, 0.5f, 0.1f);

    System.out.println("AFTER");
    System.out.println(net.toString());
    System.out.println("inp " + x);
    System.out.println("out " + net.feedforward(x));
    System.out.println("err " + net.error(trainMap));
  }

  public float error1(INDArray in, INDArray expectedOut) {
    INDArray out = feedforward(in);
    return pow(expectedOut.sub(out), 2).sumNumber().floatValue() / 2;
  }

  public double error(Map<INDArray, INDArray> validationMap) {
    return validationMap.keySet().stream().mapToDouble(x -> error1(x, validationMap.get(x))).sum();
  }
}