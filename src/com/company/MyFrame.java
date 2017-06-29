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
public class MyFrame extends JFrame {
    private JFileChooser fc = new JFileChooser();
    private JButton goButton = new JButton("GENERATE");
    private JButton saveButton = new JButton("SAVE");
    private Runnable lambda = () -> { goButton.repaint(); saveButton.repaint(); };
    private static int WIDTH = 400;
    private static int HEIGHT = 400;

    private MyFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH * 2 + 206, HEIGHT + 60 );
        setLayout(null);
        setVisible(true);
        setResizable(false);

        JLabel label = new JLabel("RGB variations (0 - 255):");
        label.setBounds(WIDTH + 5, HEIGHT,150, 30);
        label.setForeground(new Color(14,120,100));
        add(label);

        RandomPixelCanvas pc = new RandomPixelCanvas(WIDTH, HEIGHT, lambda);

        JSpinner label1 = new JSpinner(new SpinnerNumberModel(pc.rv - 1,0,255, 1));
        label1.setBounds(WIDTH + 155, HEIGHT,50, 30);
        add(label1);
        JSpinner label2 = new JSpinner(new SpinnerNumberModel(pc.gv -1,0,255, 1));
        label2.setBounds(WIDTH + 205, HEIGHT,50, 30);
        add(label2);
        JSpinner label3 = new JSpinner(new SpinnerNumberModel(pc.bv - 1,0,255, 1));
        label3.setBounds(WIDTH + 255, HEIGHT ,50, 30);
        add(label3);

        pc.setBounds(0,0, WIDTH, HEIGHT);
        add(pc);

        JColorChooser tcc = new JColorChooser(pc.baseColor);
        tcc.setBounds(WIDTH + 5,0,WIDTH + 50 + 140, HEIGHT);
        add(tcc);

        goButton.setBounds(0,HEIGHT,WIDTH / 2,30);
        goButton.addActionListener(ae -> {
            goButton.setEnabled(false);
            saveButton.setEnabled(false);
            pc.baseColor = tcc.getColor();
            pc.rv = Integer.parseInt(label1.getValue().toString()) + 1;
            pc.gv = Integer.parseInt(label2.getValue().toString()) + 1;
            pc.bv = Integer.parseInt(label3.getValue().toString()) + 1;
            setTitle("ColoRandom " + pc.info());
            pc.repaint();
            goButton.setEnabled(true);
            saveButton.setEnabled(true);
        });
        add(goButton);

        saveButton.setBounds(WIDTH / 2,HEIGHT,WIDTH / 2,30);
        saveButton.addActionListener(ae -> {
            goButton.setEnabled(false);
            saveButton.setEnabled(false);
            BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D imgg = (Graphics2D) img.getGraphics();
            pc.printAll(imgg);
            try {
                int code = fc.showSaveDialog(this);
                if(code == JFileChooser.APPROVE_OPTION)
                    ImageIO.write(img, "PNG", new File(fc.getSelectedFile().getAbsolutePath()+ ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                goButton.setEnabled(true);
                saveButton.setEnabled(true);
            }
        });
        add(saveButton);

        setTitle("ColoRandom " + pc.info());
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new MyFrame();
    }
}
