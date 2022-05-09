package com.fuzs.aquaacrobatics.optifine;

import com.fuzs.aquaacrobatics.AquaAcrobatics;

import java.lang.reflect.Field;

public class OptifineHelper {
    public static void init() {
        Class<?> reflectorMainClass;
        try {
            reflectorMainClass = Class.forName("net.optifine.reflect.Reflector");
        } catch(ClassNotFoundException e) {
            return;
        }
        AquaAcrobatics.LOGGER.info("OptiFine detected. Performing highly invasive tweaks to fix water issues.");
        try {
            Class<?> reflectorMethod = Class.forName("net.optifine.reflect.ReflectorMethod");
            Object newReflectorMethod = reflectorMethod.getConstructor(Class.forName("net.optifine.reflect.ReflectorClass"), String.class).newInstance(reflectorMainClass.getDeclaredField("ForgeBiome").get(null), "aqua$waterColorMultiplier");
            Field biomeMethodField = reflectorMainClass.getDeclaredField("ForgeBiome_getWaterColorMultiplier");
            biomeMethodField.setAccessible(true);
            biomeMethodField.set(null, newReflectorMethod);
        } catch(ReflectiveOperationException e) {
            AquaAcrobatics.LOGGER.error("An error occured while patching OptiFine", e);
            return;
        }
    }
}
