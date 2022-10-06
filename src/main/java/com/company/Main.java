package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * GUI for the canvas, adding buttons to regenerate and save and parameter inputs.
 * Created by  Armando Cordova Pelaez on 6/27/2017.
 */
public class Main {
    static int baseR1 = 248;
    static int base1G = 208;
    static int baseB1 = 207;// create an svg with the base colors for signatures
    static int baseR = 0;
    static int baseG = 0;
    static int baseB = 0;// create an svg with the base colors for signatures
    static Color initialDestiny = new Color(250, 0, 0);

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        int WIDTH = 2048;
        int HEIGHT = 1170;
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(WIDTH + 630, HEIGHT + 70 );
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(true);

        JFileChooser fc = new JFileChooser();
        JLabel label = new JLabel("RGB variations (0 - 255):");
        label.setBounds(WIDTH + 5, HEIGHT,150, 30);
        label.setForeground(initialDestiny);
        frame.add(label);

        RandomPixelCanvas2 randomPixelCanvas = new RandomPixelCanvas2(WIDTH, HEIGHT, new Color(baseR, baseG, baseB), initialDestiny);

        JSpinner spinnerR = new JSpinner(new SpinnerNumberModel(randomPixelCanvas.destinyColor.getRed(),0,255, 1));
        spinnerR.setBounds(WIDTH + 155, HEIGHT,50, 30);
        frame.add(spinnerR);
        JSpinner spinnerG = new JSpinner(new SpinnerNumberModel(randomPixelCanvas.destinyColor.getGreen(),0,255, 1));
        spinnerG.setBounds(WIDTH + 205, HEIGHT,50, 30);
        frame.add(spinnerG);
        JSpinner spinnerB = new JSpinner(new SpinnerNumberModel(randomPixelCanvas.destinyColor.getBlue(),0,255, 1));
        spinnerB.setBounds(WIDTH + 255, HEIGHT ,50, 30);
        frame.add(spinnerB);

        randomPixelCanvas.setBounds(222,222, WIDTH, HEIGHT);
        frame.add(randomPixelCanvas);

        JColorChooser tcc = new JColorChooser(randomPixelCanvas.baseColor);
        tcc.setBounds(WIDTH + 5,0,600, HEIGHT);
        frame.add(tcc);

        JButton goButton = new JButton("GENERATE");
        JButton saveButton = new JButton("SAVE");

        goButton.setBounds(0,HEIGHT,WIDTH / 2,30);
        goButton.addActionListener(ae -> {
            goButton.setEnabled(false);
            saveButton.setEnabled(false);
            randomPixelCanvas.baseColor = tcc.getColor();
            int newDestinyR = Integer.parseInt(spinnerR.getValue().toString());
            int newDestinyG = Integer.parseInt(spinnerG.getValue().toString());
            int newDestinyB = Integer.parseInt(spinnerB.getValue().toString());
            randomPixelCanvas.destinyColor = new Color(newDestinyR, newDestinyG, newDestinyB);
            frame.setTitle("Color Random " + randomPixelCanvas.info());
            randomPixelCanvas.repaint();
            goButton.setEnabled(true);
            saveButton.setEnabled(true);
        });
        frame.add(goButton);

//        saveButton.setBounds(WIDTH / 2,HEIGHT,WIDTH / 2,30);
        saveButton.setBounds(0,0,222,111);
        saveButton.addActionListener(ae -> {
            goButton.setEnabled(false);
            saveButton.setEnabled(false);
            BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D imgg = (Graphics2D) img.getGraphics();
            randomPixelCanvas.printAll(imgg);
            try {
                int code = fc.showSaveDialog(frame);
                if(code == JFileChooser.APPROVE_OPTION)
                    ImageIO.write(img, "PNG", new File(fc.getSelectedFile().getAbsolutePath()+ ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                goButton.setEnabled(true);
                saveButton.setEnabled(true);
            }
        });
        frame.add(saveButton);
        frame.setTitle("ColoRandom " + randomPixelCanvas.info());
        goButton.repaint(); saveButton.repaint();
    }
}
