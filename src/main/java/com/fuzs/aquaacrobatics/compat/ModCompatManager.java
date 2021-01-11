package com.fuzs.aquaacrobatics.compat;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import net.minecraftforge.fml.common.Loader;

public class ModCompatManager {

    private static boolean isRandomPatchesLoaded;
    private static boolean isMoBendsLoaded;
    private static boolean isWingsLoaded;
    private static boolean isArtemisLibLoaded;
    private static boolean isMorphLoaded;
    private static boolean isHatsLoaded;
    private static boolean isBetterSprintingLoaded;

    public static void loadCompat() {

        isRandomPatchesLoaded = Loader.isModLoaded("randompatches");
        isMoBendsLoaded = Loader.isModLoaded("mobends");
        isWingsLoaded = Loader.isModLoaded("wings");
        isArtemisLibLoaded = Loader.isModLoaded("artemislib");
        isMorphLoaded = Loader.isModLoaded("morph");
        isHatsLoaded = Loader.isModLoaded("hats");
        isBetterSprintingLoaded = Loader.isModLoaded("bettersprinting");
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

    public static boolean enableHatsCompat() {

        return isHatsLoaded && ConfigHandler.CompatConfig.hatsCompat;
    }

    public static boolean enableBetterSprintingCompat() {

        return isBetterSprintingLoaded && ConfigHandler.CompatConfig.betterSprintingCompat;
    }

}
