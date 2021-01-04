package com.fuzs.aquaacrobatics.compat;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import net.minecraftforge.fml.common.Loader;

public class ModCompatManager {

    private static boolean isRandomPatchesLoaded;
    private static boolean isMoBendsLoaded;
    private static boolean isWingsLoaded;
    private static boolean isArtemisLibLoaded;
    private static boolean isMorphLoaded;

    public static void loadCompat() {

        isRandomPatchesLoaded = Loader.isModLoaded("randompatches");
        isMoBendsLoaded = Loader.isModLoaded("mobends");
        isWingsLoaded = Loader.isModLoaded("wings");
        isArtemisLibLoaded = Loader.isModLoaded("artemislib");
        isMorphLoaded = Loader.isModLoaded("morph");
    }

    public static boolean enableRandomPatchesCompat() {

        return isRandomPatchesLoaded && ConfigHandler.CompatConfig.randomPatchesCompat;
    }

    public static boolean enableMoBendsCompat() {

        return isMoBendsLoaded && ConfigHandler.CompatConfig.moBendsCompat;
    }

    public static boolean enableWingsCompat() {

        return isWingsLoaded && ConfigHandler.CompatConfig.wingsCompat;
    }

    public static boolean enableArtemisLibCompat() {

        return isArtemisLibLoaded && ConfigHandler.CompatConfig.artemisLibCompat;
    }

    public static boolean enableMorphCompat() {

        return isMorphLoaded && ConfigHandler.CompatConfig.morphCompat;
    }

}
