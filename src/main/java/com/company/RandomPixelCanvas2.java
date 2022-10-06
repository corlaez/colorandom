package com.company;

import java.awt.*;
import java.util.Random;

/**
 * A Canvas that paints different every time. It uses sizes and RGB params to generate the image
 * Created by J Armando Cordova Pelaez on 6/27/2017.
 */
public class RandomPixelCanvas2 extends Canvas {
    private final Random random = new Random();
    private final int WIDTH;
    private final int HEIGHT;
    private final Runnable afterPaint;

    Color baseColor;
    Color destinyColor;
    int rv() {
        return Math.abs(destinyColor.getRed() - baseColor.getRed());
    }
    int gv() {
        return Math.abs(destinyColor.getGreen() - baseColor.getGreen());
    }
    int bv() {
        return Math.abs(destinyColor.getBlue() - baseColor.getBlue());
    }
    int rSign() {
        return destinyColor.getRed() > baseColor.getRed() ? 1 : -1;
    }
    int gSign() {
        return destinyColor.getGreen() > baseColor.getGreen() ? 1 : -1;
    }
    int bSign() {
        return destinyColor.getBlue() > baseColor.getBlue() ? 1 : -1;
    }

//    public RandomPixelCanvas2(int w, int h, Color base, Color rgbVariance, Runnable afterPaint) {
//        WIDTH = w;
//        HEIGHT = h;
//        baseColor = base;
//        rv = rgbVariance.getRed();
//        gv = rgbVariance.getGreen();
//        bv = rgbVariance.getBlue();
//        rSign = 1;
//        gSign = 1;
//        bSign = 1;
//        this.afterPaint = afterPaint;
//    }

    RandomPixelCanvas2(int w, int h, Color base, Color destiny) {
        WIDTH = w;
        HEIGHT = h;
        baseColor = base;
        destinyColor = destiny;
        setSize(w, h);
        this.afterPaint = () -> {};
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
//        int rmvf = (baseColor.getRed() > 171 ? -1 : baseColor.getRed() < 85 ? 1 : random.nextFloat() > 0.5 ? 1 : 0) * rSign();
//        int gmvf = (baseColor.getGreen() > 171 ? -1 : baseColor.getGreen() < 85 ? 1 : random.nextFloat() > 0.5 ? 1 : 0) * rSign();
//        int bmvf = (baseColor.getBlue() > 171 ? -1 : baseColor.getBlue() < 85 ? 1 : random.nextFloat() > 0.5 ? 1 : 0) * rSign();
        boolean ddd =  random.nextFloat() > 0.4;
        int nr = ddd ? baseColor.getRed() : baseColor.getRed() + (random.nextInt(rv()+1)) * rSign();
        int ng = ddd ? baseColor.getGreen() : baseColor.getGreen() + random.nextInt(gv()+1) * gSign();
        int nb = ddd ? baseColor.getBlue() : baseColor.getBlue() + random.nextInt(bv()+1) * bSign();
//        int ng = ddd ? baseColor.getGreen()  : destinyColor.getGreen() - random.nextInt(gv()+1)/4;
//        int nb = ddd ? baseColor.getBlue()  : destinyColor.getBlue() - random.nextInt(bv()+1)/4;
        if (nr > 255) nr = 255;
        if (ng > 255) ng = 255;
        if (nb > 255) nb = 255;
        if (nr < 0) nr = 0;
        if (ng < 0) ng = 0;
        if (nb < 0) nb = 0;
        return new Color(nr, ng, nb);
    }

    String info() {
        return baseColor.getRed() + "(+" + rv() * rSign() + ") " + baseColor.getGreen() + "(+" + gv() * gSign() + ") " + baseColor.getBlue() + "(+" + bv() * bSign() + ")";
    }
}
