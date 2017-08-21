package com.company.mnist;

import java.io.*;
import java.util.*;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Created by jarma on 8/20/2017.
 */
public class MnistReader {

  static int FORCE_OUT = -1;
  static String FOLDER = "D:\\MyCustomFolders\\MyDesktop\\numbers\\mnist";
  static String TRAIN_IMAGES = FOLDER + "\\train-images.idx3-ubyte";
  static String TRAIN_LABELS = FOLDER + "\\train-labels.idx1-ubyte";
  static String TEST_IMAGES = FOLDER + "\\t10k-images.idx3-ubyte";
  static String TEST_LABELS = FOLDER + "\\t10k-labels.idx1-ubyte";

  Map<INDArray, INDArray> loadTrainMap(){
    return loadMap(TRAIN_IMAGES, TRAIN_LABELS);
  }

  Map<INDArray, INDArray> loadTestMap(){
    return loadMap(TEST_IMAGES, TEST_LABELS);
  }

  Map<INDArray, INDArray> loadMap(String imagesPath, String labelsPath) {
    Map<INDArray, INDArray> map = new HashMap<>();
    try (DataInputStream imageStream = new DataInputStream(new FileInputStream(imagesPath));
        DataInputStream labelStream = new DataInputStream(new FileInputStream(labelsPath))) {
      int imageMagic = imageStream.readInt();
      int labelMagic = labelStream.readInt();
      int imageN = imageStream.readInt();
      int labelN = labelStream.readInt();
      if (labelMagic != 2049 || imageMagic != 2051) {
        throw new RuntimeException("Incorrect or corrupt file.");
      }
      if (labelN != imageN) {
        throw new RuntimeException("Size of labels is diferent from size of images.");
      }
      int imageRows = imageStream.readInt();
      int imageCols = imageStream.readInt();
      System.out.println("N = " + imageN);
      for (int i = 0; i < imageN; i++) {
        INDArray image = getImage(imageStream, imageRows, imageCols);
        INDArray label = getLabel(labelStream);
        map.put(image, label);
        if (i == FORCE_OUT) {
          throw new RuntimeException("FORCE OUT");
        }
      }
    } catch (RuntimeException e) {
      System.out.println("FORCED OUT ON " + FORCE_OUT);
      return null;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return map;
  }

  private INDArray getImage(DataInputStream stream, int rows, int cols) throws IOException {
    byte[] imageBytes = new byte[rows * cols];
    stream.read(imageBytes);
    //TRANSFORM TO FLOAT
    float[] floatArray = new float[imageBytes.length];
    for (int i = 0; i < imageBytes.length; i++) {
      floatArray[i] = imageBytes[i];
      if (i % rows == 0) {
        System.out.println();
      }
      int val = imageBytes[i];
      String formated = String.format("%-3x", imageBytes[i]);
      if (val != 0) {
        System.out.printf("%-" + formated.length() + "s", formated);
      } else {
        System.out.printf("%-" + formated.length() + "s", "");
      }
    }
    return Nd4j.create(new int[]{rows, cols}, floatArray);
  }

  private INDArray getLabel(DataInputStream stream) throws IOException {
    int l = stream.readByte();
    System.out.print(l);
    float[] lArr = new float[10];
    lArr[l] = 1;
    return Nd4j.create(lArr);
  }

  public static void main(String[] args) throws Exception {
    MnistReader reader = new MnistReader();
    Map<INDArray, INDArray> trainMap = reader.loadTrainMap();
    Map<INDArray, INDArray> loadTestMap = reader.loadTestMap();
  }
}