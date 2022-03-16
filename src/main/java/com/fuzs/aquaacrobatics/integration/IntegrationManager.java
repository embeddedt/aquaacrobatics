package com.fuzs.aquaacrobatics.integration;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import net.minecraftforge.fml.common.Loader;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

public class IntegrationManager {

    private static boolean isBetweenlandsLoaded;
    private static boolean isChiseledMeLoaded;
    private static boolean isEnderIoLoaded;
    private static boolean isRandomPatchesLoaded;
    private static boolean isMoBendsLoaded;
    private static boolean isWingsLoaded;
    private static boolean isArtemisLibLoaded;
    private static boolean isMorphLoaded;
    private static boolean isHatsLoaded;
    private static boolean isThaumicAugmentationLoaded;
    private static boolean isTrinketsAndBaublesLoaded;
    
    public static List<IElytraOpenHook> elytraOpenHooks = new LinkedList<>();

    public static void loadCompat() {
        isBetweenlandsLoaded = Loader.isModLoaded("thebetweenlands");
        isChiseledMeLoaded = Loader.isModLoaded("chiseled_me");
        isEnderIoLoaded = Loader.isModLoaded("enderio");
        isRandomPatchesLoaded = Loader.isModLoaded("randompatches");
        isMoBendsLoaded = Loader.isModLoaded("mobends");
        isWingsLoaded = Loader.isModLoaded("wings");
        isArtemisLibLoaded = Loader.isModLoaded("artemislib");
        isMorphLoaded = Loader.isModLoaded("morph");
        isHatsLoaded = Loader.isModLoaded("hats");
        isThaumicAugmentationLoaded = Loader.isModLoaded("thaumicaugmentation");
        isTrinketsAndBaublesLoaded = Loader.isModLoaded("xat");
    }

    public static boolean isBetweenlandsEnabled() {

        return isBetweenlandsLoaded && ConfigHandler.IntegrationConfig.betweenlandsIntegration;
    }

    public static boolean isChiseledMeEnabled() {

        return isChiseledMeLoaded && ConfigHandler.IntegrationConfig.chiseledMeIntegration;
    }

    public static boolean isEnderIoEnabled() {

        return isEnderIoLoaded && ConfigHandler.IntegrationConfig.enderIoIntegration;
    }

    public static boolean isRandomPatchesEnabled() {

        return isRandomPatchesLoaded && ConfigHandler.IntegrationConfig.randomPatchesIntegration;
    }

    public static boolean isMoBendsEnabled() {

        return isMoBendsLoaded && ConfigHandler.IntegrationConfig.moBendsIntegration;
    }

    public static boolean isWingsEnabled() {

        return isWingsLoaded && ConfigHandler.IntegrationConfig.wingsIntegration;
    }

    public static boolean isArtemisLibEnabled() {

        return isArtemisLibLoaded && ConfigHandler.IntegrationConfig.artemisLibIntegration;
    }

    public static boolean isMorphEnabled() {

        return isMorphLoaded && ConfigHandler.IntegrationConfig.morphIntegration;
    }

    public static boolean isHatsEnabled() {

        return isHatsLoaded && ConfigHandler.IntegrationConfig.hatsIntegration;
    }

    public static boolean isThaumicAugmentationEnabled() {

        return isThaumicAugmentationLoaded && ConfigHandler.IntegrationConfig.thaumicAugmentationIntegration;
    }

    public static boolean isTrinketsAndBaublesEnabled() {

        return isTrinketsAndBaublesLoaded && ConfigHandler.IntegrationConfig.trinketsAndBaublesIntegration;
    }
}
