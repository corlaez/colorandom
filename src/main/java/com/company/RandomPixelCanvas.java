package com.company;

import java.awt.*;
import java.util.Random;
import java.util.function.Supplier;

/**
 * A Canvas that paints different every time. It uses sizes and RGB params to generate the image
 * Created by J Armando Cordova Pelaez on 6/27/2017.
 */
public class RandomPixelCanvas extends Canvas {
    private Random random = new Random();
    private int WIDTH = 400;
    private int HEIGHT = 400;
    private Runnable afterPaint;

    int rv = 60;
    int gv = 96;
    int bv = 76;
    private double TH = 0.02;
    Color baseColor = new Color(8, 144, 117);

    public RandomPixelCanvas(int w, int h, Color base, Color rgbVariance, Runnable afterPaint) {
        WIDTH = w;
        HEIGHT = h;
        baseColor = base;
        rv = rgbVariance.getRed();
        gv = rgbVariance.getGreen();
        bv = rgbVariance.getBlue();
        this.afterPaint = afterPaint;
    }

    RandomPixelCanvas(int w, int h, Runnable afterPaint) {
        WIDTH = w;
        HEIGHT = h;
        baseColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        rv = random.nextInt(256) + 1;
        gv = random.nextInt(256) + 1;
        bv = random.nextInt(256) + 1;
        this.afterPaint = afterPaint;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        do {
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y += 1) {
                    g.setColor(mostVariantColor(() -> 1));
                    g.drawLine(x, y, x, y);
                }
            }
        } while (random.nextDouble() > TH);
        if (afterPaint != null)
            afterPaint.run();
    }

    private Color mostVariantColor(Supplier<Integer> multiplyVariance) {
        int rmvf = (baseColor.getRed() > 171 ? -1 : baseColor.getRed() < 85 ? 1 : random.nextFloat() > 0.5 ? 1 : 0);
        int gmvf = (baseColor.getGreen() > 171 ? -1 : baseColor.getGreen() < 85 ? 1 : random.nextFloat() > 0.5 ? 1 : 0);
        int bmvf = (baseColor.getBlue() > 171 ? -1 : baseColor.getBlue() < 85 ? 1 : random.nextFloat() > 0.5 ? 1 : 0);
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

    String info() {
        return baseColor.getRed() + "(+" + rv + ") " + baseColor.getGreen() + "(+" + gv + ") " + baseColor.getBlue() + "(+" + bv + ")";
    }
}
