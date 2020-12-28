package com.fuzs.aquaacrobatics;

import com.fuzs.aquaacrobatics.compat.CompatibilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings("unused")
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

    @Mod.EventHandler
    public void onPreInit(final FMLPreInitializationEvent evt) {

        CompatibilityManager.init();
        CompatibilityManager.apply(CompatibilityManager.OBFUSCATE_ID);
    }

}
