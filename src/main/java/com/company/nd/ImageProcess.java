package com.company.nd;

import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.InvertFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import org.datavec.image.loader.ImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Created by jarma on 8/8/2017.
 */
public class ImageProcess {

  static INDArray invGrayReducePrint(String pathname, String extension, int f1f2w3c4)
      throws Exception {
    String format = "%-6x";
    //open image
    File file = new File(pathname + "." + extension);
    BufferedImage img = ImageIO.read(file);
//    img = new ().filter(img, img);
    //apply filters
    if ((f1f2w3c4 & 8) == 8) {
      img = new GrayscaleFilter().filter(img, img);
    }
    if ((f1f2w3c4 & 4) == 4) {
      img = new InvertFilter().filter(img, img);
    }
    //write file
    if ((f1f2w3c4 & 2) == 2) {
      String parent = Paths.get(pathname).getParent().toString();
      String newFileName = file.getName();
      newFileName = newFileName.substring(0, newFileName.length() - 1 - extension.length());
      newFileName += "0b" + Integer.toBinaryString(f1f2w3c4) + "." + extension;
      File newFile = new File(parent + "\\out\\" + newFileName);
      ImageIO.write(img, extension, newFile);
    }
    //load to array
    int h = img.getHeight();
    int w = img.getWidth();
    int ch = 4;
    ImageLoader loader = new ImageLoader(h, w, ch);
    INDArray arr = loader.asMatrix(img);
    //if Grayscale then simplify matrix
    if ((f1f2w3c4 & 8) == 8) {
      format = "%-3x";
      INDArray arrR = Nd4j.zeros(h, w);
      for (int i = 0; i < arr.rows(); i++) {
        for (int j = 0; j < arr.getRow(i).length(); j++) {
//        int clr = img.getRGB(j, i);
//        int alf = (clr & 0xff000000) >>> 24;
//        int red = (clr & 0x00ff0000) >>> 16;
//        int green = (clr & 0x0000ff00) >>> 8;
//        int blue = clr & 0x000000ff;
          //for each hex RGB replace with hex of any color
          int newVal = arr.getRow(i).getInt(j) & 0x000000ff;
          arrR.getRow(i).getColumn(j).addi(newVal);
        }
      }
      arr = arrR;
    }
    if ((f1f2w3c4 & 1) == 1) {
      printf(arr, format);
    }
    return arr;
  }

  private static void printf(INDArray arr, String format) {
    for (int i = 0; i < arr.rows(); i++) {
      for (int j = 0; j < arr.getRow(i).length(); j++) {
        int val = arr.getRow(i).getInt(j) & 0x00_ff_ff_ff;//remove alpha
        String formated = String.format(format, val);
        if (val != 0) {
          System.out.printf("%-" + formated.length() + "s", formated);
        } else {
          System.out.printf("%-" + formated.length() + "s", ".");
        }
      }
      System.out.println();
    }
  }

  public static void main(String[] args) throws Exception {
    String pathname = "D:\\MyCustomFolders\\MyDesktop\\numbers\\";
    String[] pngs = new String[]{"kathehermosa.jpg", "llamamini.png", "5_28_28.png"};
//    pngs = new String[0];
//    pngs = new String[]{"5_28_28.png"};
    pngs = new String[]{"fire.jpg"};
//    pngs = new String[]{"5_28_28.png", "64.png", "16.png"};
    for (String s : pngs) {
      invGrayReducePrint(pathname + s.split("\\.")[0], s.split("\\.")[1], 0b1010);
      System.out.println();
    }
    for (int i = 10; i < 10 ; i++) {
      invGrayReducePrint(pathname + i, "png", 0b1111);
      System.out.println();
    }
  }
}