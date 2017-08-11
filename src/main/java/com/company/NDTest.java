package com.company;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Created by jarma on 8/8/2017.
 */
public class NDTest {

  public static void main(String[] args) {
    INDArray arr = Nd4j.rand(2, 3);
    System.out.println(arr);
  }

}
