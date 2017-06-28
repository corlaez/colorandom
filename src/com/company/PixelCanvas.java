package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Created by J. Armando Cordova on 6/27/2017.
 */
public class PixelCanvas extends Canvas {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final Random random = new Random();

    private Color baseColor = new Color(8, 144, 117);
    private int rv = 60;
    private int gv = 96;
    private int bv = 76;
    private Runnable afterPaint;
    private JFileChooser fc = new JFileChooser();
    private int o = 3;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        switch (o){
            case 0:
                do {
                    for(int x = 0; x < WIDTH; x++) {
                        for( int y = 0; y < HEIGHT; y+=1) {
                            g.setColor(customColor(() -> 1));
                            g.drawLine(x, y, x,y);
                        }
                    }
                } while(random.nextDouble() > 0.02);
                break;
            case 1:
                do {
                    for(int x = 0; x < WIDTH; x++) {
                        for( int y = 0; y < HEIGHT; y+=1) {
                            g.setColor(customColor(() -> -1));
                            g.drawLine(x, y, x,y);
                        }
                    }
                } while(random.nextDouble() > 0.02);
                break;
            case 2:
                do {
                    for(int x = 0; x < WIDTH; x++) {
                        for( int y = 0; y < HEIGHT; y+=1) {
                            g.setColor(customColor(() -> HEIGHT % 2 == 0 ? 1 : -1));
                            g.drawLine(x, y, x,y);
                        }
                    }
                } while(random.nextDouble() > 0.02);
                break;
            case 3:
                do {
                    for(int x = 0; x < WIDTH; x++) {
                        for( int y = 0; y < HEIGHT; y+=1) {
                            g.setColor(mostVariantColor(() -> 1));
                            g.drawLine(x, y, x,y);
                        }
                    }
                } while(random.nextDouble() > 0.02);
                break;
        }
        if(afterPaint != null)
            afterPaint.run();
    }

    private Color mostVariantColor(Supplier<Integer> multiplyVariance) {
        int rmvf = (baseColor.getRed() > 171 ? -1 : baseColor.getRed() < 85 ? 1 : random.nextFloat() > 0.5 ? 1 : 0 );
        int gmvf = (baseColor.getGreen() > 171 ? -1 : baseColor.getGreen() < 85 ? 1 : random.nextFloat() > 0.5 ? 1 : 0 );
        int bmvf = (baseColor.getBlue() > 171 ? -1 : baseColor.getBlue() < 85 ? 1 : random.nextFloat() > 0.5 ? 1 : 0 );
        int nr = baseColor.getRed() + rmvf * random.nextInt(rv) * multiplyVariance.get();
        int ng = baseColor.getGreen() + gmvf * random.nextInt(gv) * multiplyVariance.get();
        int nb = baseColor.getBlue() + bmvf * random.nextInt(bv) * multiplyVariance.get();
        if (nr > 255) nr = 255;
        if (ng > 255) ng = 255;
        if (nb > 255) nb = 255;
        if (nr < 0) nr = 0;
        if (ng < 0) ng = 0;
        if (nb < 0) nb = 0;
        return new Color(nr, ng, nb);
    }

    private Color customColor(Supplier<Integer> multiplyVariance) {
        int nr = baseColor.getRed() + random.nextInt(rv) * multiplyVariance.get();
        int ng = baseColor.getGreen() + random.nextInt(gv) * multiplyVariance.get();
        int nb = baseColor.getBlue() + random.nextInt(bv) * multiplyVariance.get();
        if (nr > 255) nr = 255;
        if (ng > 255) ng = 255;
        if (nb > 255) nb = 255;
        if (nr < 0) nr = 0;
        if (ng < 0) ng = 0;
        if (nb < 0) nb = 0;
        return new Color(nr, ng, nb);
    }

    public static void main(String[] args) {
        Locale.setDefault(new Locale("ES","PE"));
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH * 2 + 66 + 140, HEIGHT + 60 );
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);

        JButton goButton = new JButton("GENERAR");
        JButton saveButton = new JButton("GRABAR");

        JLabel label = new JLabel("Variaciones (1 - 255):");
        label.setBounds(WIDTH + 5, HEIGHT,125, 30);
        label.setForeground(new Color(14,120,100));
        frame.getContentPane().add(label);

        JSpinner label1 = new JSpinner(new SpinnerNumberModel(60,1,256, 1));
        label1.setBounds(WIDTH + 130, HEIGHT,50, 30);
        frame.getContentPane().add(label1);
        JSpinner label2 = new JSpinner(new SpinnerNumberModel(96,1,256, 1));
        label2.setBounds(WIDTH + 180, HEIGHT,50, 30);
        frame.getContentPane().add(label2);
        JSpinner label3 = new JSpinner(new SpinnerNumberModel(76,1,256, 1));
        label3.setBounds(WIDTH + 230, HEIGHT ,50, 30);
        frame.getContentPane().add(label3);

        PixelCanvas pc = new PixelCanvas();
        pc.afterPaint = () -> { goButton.repaint(); saveButton.repaint(); };
        pc.setBounds(0,0, WIDTH, HEIGHT);
        frame.getContentPane().add(pc);

        JColorChooser tcc = new JColorChooser(pc.baseColor);
        tcc.setBounds(WIDTH + 5,0,WIDTH + 50 + 140, HEIGHT);
        frame.getContentPane().add(tcc);

        goButton.setBounds(0,HEIGHT,WIDTH / 2,30);
        goButton.addActionListener(ae -> {
            goButton.setEnabled(false);
            saveButton.setEnabled(false);
            pc.baseColor = tcc.getColor();
            pc.rv = Integer.parseInt(label1.getValue().toString());
            pc.gv = Integer.parseInt(label2.getValue().toString());
            pc.bv = Integer.parseInt(label3.getValue().toString());
            frame.setTitle("Juego de Color " + pc.info());
            pc.repaint();
            goButton.setEnabled(true);
            saveButton.setEnabled(true);
        });
        frame.getContentPane().add(goButton);

        saveButton.setBounds(WIDTH / 2,HEIGHT,WIDTH / 2,30);
        saveButton.addActionListener(ae -> {
            goButton.setEnabled(false);
            saveButton.setEnabled(false);
            BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D imgg = (Graphics2D) img.getGraphics();
            pc.printAll(imgg);
            try {
                int code = pc.fc.showSaveDialog(frame);
                if(code == JFileChooser.APPROVE_OPTION)
                    ImageIO.write(img, "PNG", new File(pc.fc.getSelectedFile().getAbsolutePath()+ ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                goButton.setEnabled(true);
                saveButton.setEnabled(true);
            }
        });
        frame.getContentPane().add(saveButton);

        frame.setTitle("Juego de Color " + pc.info());
    }

    private String info() {
        return baseColor.getRed() + "(+" + rv  + ") " +  baseColor.getGreen() + "(+" + gv + ") " +  baseColor.getBlue() + "(+" + bv + ")";
    }
}
