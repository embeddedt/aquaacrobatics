package com.fuzs.fivefeetsmall.util;

public class MathHelper extends net.minecraft.util.math.MathHelper {

    public static float lerp(float pct, float start, float end) {
        return start + pct * (end - start);
    }

    public static double lerp(double pct, double start, double end) {
        return start + pct * (end - start);
    }

}
