package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * A visual linear regression
 * Created by J Armando Cordova Pelaez on 6/27/2017.
 */
public class LinearRegression {
    private static Random random = new Random();
    private static int iter;
    private Map<Integer, Integer> points;
    private double m = 1;
    private double b = 1;
    private double error;

    {
        points = new TreeMap<>();
        int max = 400;
        for (int i = 10; i < max - 10; i += random.nextInt(5)) {
            points.put(i, 50 + random.nextInt(max) / 5);
        }
        for (Integer y = points.values().stream().max(Integer::compareTo).get(); y >= 0; y--) {
            for (int x = 0; x < max; x++) {
                if (points.get(x).equals(y))
                    System.out.print("@");
                else System.out.print("-");
            }
            System.out.println();
        }
    }

    public Double getBestFitY(double x) {
        return m * x + b;
    }

    public void printAll(Graphics g) {
        g.setColor(Color.black);
        g.drawLine(0, getBestFitY(0).intValue(), 400, getBestFitY(400).intValue());
        g.setColor(Color.black);
        points.entrySet().forEach(e ->
                g.drawLine(e.getKey(), e.getValue(), e.getKey(), e.getValue())
        );
    }


    public static void main(String[] args) {
        LinearRegression le = new LinearRegression();
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.setLayout(null);
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);//white
                le.printAll(g);//line
                le.linearRegression();//gradient descent
                if (iter++ < 1000) repaint();//loop
            }
        };
        panel.setBounds(0, 0, 400, 400);
        frame.add(panel);
        frame.setVisible(true);

        JButton btn = new JButton("NEXT");
        btn.setBounds(0, 0, 100, 20);
        btn.addActionListener(ae -> {
                    panel.repaint();
                    frame.setTitle("" + le.error);
                }
        );
        frame.add(btn);
    }

    private void linearRegression() {
        error = error();
        m = random.nextDouble();
        b = random.nextInt(200);
    }

    private double error() {
        return points.entrySet().stream().map(e -> {
                    int px = e.getKey();
                    int py = e.getValue();
                    double fy = getBestFitY(px);
                    double distance = Math.pow(Math.pow(fy - py, 2), 1);
                    return distance;
                }
        ).reduce(0d, (a, b) -> a + b);
    }

}
