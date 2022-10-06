package com.company;

import java.awt.*;
import java.util.Random;

/**
 * A Canvas that paints different every time. It uses sizes and RGB params to generate the image
 * Created by J Armando Cordova Pelaez on 6/27/2017.
 */
public class RandomPixelCanvas extends Canvas {
    private final Random random = new Random();
    private final int WIDTH;
    private final int HEIGHT;
    private final Runnable afterPaint;

    int rv;
    int gv;
    int bv;
    private double TH = 0.02;
    Color baseColor;

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
        baseColor = new Color(248, 208, 207);
        rv = 255;
        gv = 0;
        bv = 0;
        setSize(w, h);
        this.afterPaint = afterPaint;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y += 1) {
                g.setColor(mostVariantColor());
                g.drawLine(x, y, x, y);
            }
        }
        if (afterPaint != null)
            afterPaint.run();
    }

    private Color mostVariantColor() {
        // allows the variation to 'bounce back'. without this white wouldnt variate at all as RGB is maxed out.
        int rmvf = (baseColor.getRed() > 171 ? -1 : baseColor.getRed() < 85 ? 1 : random.nextFloat() > 0.5 ? 1 : 0);
        int gmvf = (baseColor.getGreen() > 171 ? -1 : baseColor.getGreen() < 85 ? 1 : random.nextFloat() > 0.5 ? 1 : 0);
        int bmvf = (baseColor.getBlue() > 171 ? -1 : baseColor.getBlue() < 85 ? 1 : random.nextFloat() > 0.5 ? 1 : 0);
        int nr = baseColor.getRed() + rmvf * random.nextInt(rv+1) * (random.nextFloat() > 0.5 ? 1 : 0);
        int ng = baseColor.getGreen() + gmvf * random.nextInt(gv+1) * (random.nextFloat() > 0.5 ? 1 : 0);
        int nb = baseColor.getBlue() + bmvf * random.nextInt(bv+1) * (random.nextFloat() > 0.5 ? 1 : 0);
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
