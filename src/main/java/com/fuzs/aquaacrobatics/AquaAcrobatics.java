package com.fuzs.aquaacrobatics;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
@Mod(
        modid = AquaAcrobatics.MODID,
        name = AquaAcrobatics.NAME,
        version = AquaAcrobatics.VERSION,
        acceptedMinecraftVersions = "[1.12.2]"
)
public class AquaAcrobatics {

    public static final String MODID = "aquaacrobatics";
    public static final String NAME = "Aqua Acrobatics";
    public static final String VERSION = "1.1";

    private static boolean isRandomPatchesLoaded;
    private static boolean isMoBendsLoaded;
    private static boolean isWingsLoaded;

    @Mod.EventHandler
    public void onPreInit(final FMLPreInitializationEvent evt) {

        isRandomPatchesLoaded = Loader.isModLoaded("randompatches");
        isMoBendsLoaded = Loader.isModLoaded("mobends");
        isWingsLoaded = Loader.isModLoaded("wings");
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

}
