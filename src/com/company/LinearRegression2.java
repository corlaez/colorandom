package com.company;

import com.company.math.LinearFunction;
import com.company.math.PointS;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A visual linear regression
 * Created by J Armando Cordova Pelaez on 6/27/2017.
 */
public class LinearRegression2 {
    private static Random random = new Random();
    private static int iter;
    private List<PointS> points = new ArrayList<>();
    private LinearFunction line = new LinearFunction(-1, 0);

    public void printAll(Graphics g) {
        g.setColor(Color.BLUE);
        int y0 = line.getIntY(0);
        int y400 = line.getIntY(400);
        g.drawLine(0, y0, 400, y400);
        g.setColor(Color.RED);
        points.forEach(p -> g.drawLine(p.ix, p.iy, p.ix, p.iy));
    }


    public static void main(String[] args) {
        LinearRegression2 le = new LinearRegression2();
        JFrame frame = new JFrame();
        frame.setSize(400, 425);
        frame.setLayout(null);
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);//white
                boolean out = false;
                while (!out) {
                    try {
                        le.printAll(g);//points and line
                        out = true;
                    } catch (Exception e) {
                        out = true;
                        le.line.adjustGamma(null);
                    }
                }
                if (iter++ < 5000) {//as low as 200 - 250
                    le.line.linearRegression(le.points);//gradient descent (adjust)
                    le.logInfo();//log
                    repaint();//loop
                } else frame.setTitle("Error: " + le.error());
            }
        };
        panel.setBackground(Color.WHITE);
        panel.setBounds(0, 0, 400, 400);
        frame.add(panel);
        frame.setVisible(true);
        frame.setResizable(false);

        JButton btn = new JButton("RESET");
        btn.setBounds(0, 0, 100, 20);
        btn.addActionListener(ae -> {
//                    le.line.setM(random.nextInt(60) * (random.nextBoolean() ? 1 : -1));
//                    le.line.setB(random.nextInt(400));
                    le.line.setM(-1);
                    le.line.setB(0);
                    iter = 0;
                    System.out.println();
                    panel.repaint();
                }
        );
        frame.add(btn);

        JButton btn2 = new JButton("UP POINTS");
        btn2.setBounds(100, 0, 100, 20);
        btn2.addActionListener(ae -> {
                    le.points.clear();
                    int max = 400;
                    for (int i = 10; i < max - 10; i += 5 + random.nextInt(10)) {
                        le.points.add(new PointS(i, 0 + random.nextInt(max) / 5));
                    }
                    le.line.setM(-1);
                    le.line.setB(0);
                    panel.repaint();
                }
        );
        frame.add(btn2);

        JButton btn3 = new JButton("RANDOM POINTS");
        btn3.setBounds(200, 0, 100, 20);
        btn3.addActionListener(ae -> {
                    le.points.clear();
                    int max = 400;
                    for (int i = 0; i < max; i += 5 + random.nextInt(10)) {
                        le.points.add(new PointS(i, random.nextInt(max)));
                    }
                    le.line.setB(0);
                    le.line.setM(1);
                    panel.repaint();
                }
        );
        frame.add(btn3);
    }

    private double error() {
        return line.getError(points);
    }

    private void logInfo() {
        if (iter % 200 == 0 && iter > 4000)
            System.out.println("At step " + iter + " - Line: y = " + line.getM() + " x + " + line.getB() + " and Error: " + line.getError(points));
    }

}
