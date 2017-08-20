package com.company.nd;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Created by jarma on 8/9/2017.
 */
public class NDLayer {

  public static String DEFAULT_ACTIVATION = "leakyrelu";//https://github.com/deeplearning4j/nd4j/blob/aafbd0101b1a10a0d4dae1a2671895410a1708b2/nd4j-backends/nd4j-tests/src/test/java/org/nd4j/linalg/ops/DerivativeTests.java

  //metadata
  int index = -1;//if set the index will be printed on toStrings
  int nWeightsPerNeuron;//computed for clarity
  int nNeurons;//computed for clarity
  //data
  INDArray weights;//bidimensional data array
  INDArray biases;//data vector
  UnaryOperator<INDArray> activationFunc;
  UnaryOperator<INDArray> activationFuncDerivative;
  //backprop
  INDArray netInput;
  INDArray activation;
  INDArray weightsNabla;
  INDArray biasesNabla;

  public NDLayer(int nWeightsPerNeuron, int nNeurons) {
    this.nNeurons = nNeurons;
    this.nWeightsPerNeuron = nWeightsPerNeuron;
    this.weights = Nd4j.randn(new int[]{nNeurons, nWeightsPerNeuron});
    this.biases = Nd4j.randn(new int[]{nNeurons, 1});
    setActivationName(DEFAULT_ACTIVATION);
    weightsNabla = Nd4j.zerosLike(weights);
    biasesNabla = Nd4j.zerosLike(biases);
  }

  public NDLayer(int index, int numberOfWeightsPerNeuron, int numberOfNeurons) {
    this(numberOfWeightsPerNeuron, numberOfNeurons);
    this.index = index;
  }

  public INDArray net(INDArray input) {
    if (nWeightsPerNeuron != input.size(0)) {
      throw new IllegalArgumentException("wrong input");
    }
    netInput = weights.mmul(input).add(biases);
    return netInput;
  }

  public INDArray activate(INDArray input) {
    activation = activationFunc.apply(net(input));
    return activation;
  }

  public void setActivationName(String activation) {
    this.activationFunc = x -> Nd4j.getExecutioner().execAndReturn(
        Nd4j.getOpFactory().createTransform(activation, x));
    this.activationFuncDerivative = x -> Nd4j.getExecutioner().execAndReturn(
        Nd4j.getOpFactory().createTransform(activation, x).derivative());
  }
  //backprop
  INDArray delta(INDArray result, INDArray target) {
    return (result.sub(target)).mul(activationFuncDerivative.apply(result));
  }

  void updateLayer(float eta, int groupSize) {
    weights.subi(weightsNabla.mul(eta / groupSize));
    biases.subi(biasesNabla.mul(eta / groupSize));
    weightsNabla = Nd4j.zerosLike(weights);
    biasesNabla = Nd4j.zerosLike(biases);
  }

  @Override
  public String toString() {
    String s = "Layer" + (index > -1 ? " " + index : "") + ":\n";
    for (int i = 0; i < nNeurons; i++) {
      s += "N" + i + ": w " + weights.getRow(i) + ", b " + biases.getRow(i) + "\n";
    }
    return s;
  }

  public String toStringMini() {
    String s = "Layer" + (index > -1 ? " " + index : "") + "(ns: ";
    s += nNeurons + ", ws: " + nWeightsPerNeuron + ")";
    return s;
  }

  public static void main(String[] args) {
    NDLayer layer = new NDLayer(5, 5);
    INDArray input = Nd4j.rand(new int[]{5, 1});
    System.out.println("Antes de entrenar");
    System.out.println(layer);
    INDArray dif1 = input.sub(layer.activate(input));
    System.out.println("dif " + dif1);
    for (int i = 0; i < 1500; i++) {
      layer.gradientDescent(input, input, 0.5f);
    }
    System.out.println("\nLuego de entrenar");
    System.out.println(layer);
    INDArray dif2 = input.sub(layer.activate(input));
    System.out.println("dif " + dif2);
  }

  public void gradientDescent(INDArray in, INDArray target, float learningRate) {
    INDArray bG = delta(in, target);
    INDArray wG = bG.mmul(in.transpose());
    weights.subi(wG.mul(learningRate));
    biases.subi(bG.mul(learningRate));
  }
}