package com.company.nd;

import java.util.function.UnaryOperator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.distribution.impl.NormalDistribution;
import org.nd4j.linalg.factory.Nd4j;

import static org.nd4j.linalg.ops.transforms.Transforms.*;

/**
 * Created by jarma on 8/9/2017.
 */
public class NDLayer {

  int index = -1;
  int nWeightsPerNeuron;
  int nNeurons;
  INDArray weights;
  INDArray biases;
  UnaryOperator<INDArray> activationFunction;

  public NDLayer(int nWeightsPerNeuron, int nNeurons) {
    this.nNeurons = nNeurons;
    this.nWeightsPerNeuron = nWeightsPerNeuron;
    weights = Nd4j.rand(new int[]{nNeurons, nWeightsPerNeuron}, new NormalDistribution());
    biases = Nd4j.rand(new int[]{nNeurons, 1}, new NormalDistribution());
    setActivationName(null);
  }

  public NDLayer(int index, int numberOfWeightsPerNeuron, int numberOfNeurons) {
    this(numberOfWeightsPerNeuron, numberOfNeurons);
    this.index = index;
  }

  public INDArray activate(INDArray input) {
    INDArray output = weights.mmul(input).add(biases);
    output = activationFunction.apply(output);
    return output;
  }

  public int getnNeurons() {
    int r = weights.size(0);
    if (r != nNeurons) {
      throw new RuntimeException("NEURONS NUMBER IS WRONG: " + r + " != " + nNeurons);
    }
    return r;
  }

  public int getnWeigthsPerNeuron() {
    int r = weights.getRow(0).length();
    if (r != nWeightsPerNeuron) {
      throw new RuntimeException("WPN NUMBER IS WRONG: " + r + " != " + nWeightsPerNeuron);
    }
    return r;
  }

  public void setActivationName(String activationNameP) {
    if (activationNameP == null) {
      activationFunction = x -> sigmoid(x);
      return;
    }
    activationFunction = x -> Nd4j.getExecutioner().exec(
        Nd4j.getOpFactory().createTransform(activationNameP, x)).z();
  }

  @Override
  public String toString() {
    String s = "Layer" + (index > -1 ? " " + index : "") + ":\n";
    for (int i = 0; i < nNeurons; i++) {
      s += "Neuron " + i + ": Weights " + weights.getRow(i) +
          ", bias " + biases.getRow(i) + "\n";
    }
    return s;
  }

  public String toStringShort() {
    String s = "Layer" + (index > -1 ? " " + index : "") + ":\n";
    for (int i = 0; i < nNeurons; i++) {
      s += "N" + i + "(" + nWeightsPerNeuron + ", " + biases.getRow(i) + ")\n";
    }
    return s;
  }

  public String toStringShortest() {
    String s = "Layer" + (index > -1 ? " " + index : "") + ":\n";
    for (int i = 0; i < nNeurons; i++) {
      s += "N" + i + "(" + nWeightsPerNeuron + ")\n";
    }
    return s;
  }

  public String toStringMini() {
    String s = "Layer" + (index > -1 ? " " + index : "") + "(ns: ";
    s += nNeurons + ", ws: " + nWeightsPerNeuron + ")";
    return s;
  }

  public static void main(String[] args) {
    NDLayer layer = new NDLayer(20, 2);
    INDArray input = Nd4j.rand(new int[]{20, 1}, new NormalDistribution());
    System.out.println(layer.activate(input));
  }
}