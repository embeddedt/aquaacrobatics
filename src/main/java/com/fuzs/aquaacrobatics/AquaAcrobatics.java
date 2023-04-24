package com.fuzs.aquaacrobatics;

import com.fuzs.aquaacrobatics.core.AquaAcrobaticsCore;
import com.fuzs.aquaacrobatics.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
@Mod(
        modid = AquaAcrobatics.MODID,
        name = AquaAcrobatics.NAME,
        version = AquaAcrobatics.VERSION,
        acceptedMinecraftVersions = "[1.12.2]",
        acceptableRemoteVersions = "*",
        dependencies = "before:mobends@(0.24,)"
)
public class AquaAcrobatics {

    public static final String MODID = "aquaacrobatics";
    public static final String NAME = "Aqua Acrobatics";
    public static final String VERSION = Tags.VERSION;
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    private static final String CLIENT_PROXY = "com.fuzs." + MODID + ".proxy.ClientProxy";
    private static final String COMMON_PROXY = "com.fuzs." + MODID + ".proxy.CommonProxy";

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    private static CommonProxy proxy;

    @Mod.EventHandler
    public void onPreInit(final FMLPreInitializationEvent evt) {

        if (AquaAcrobaticsCore.isLoaded()) {

            proxy.onPreInit(evt);
        }
    }

    @Mod.EventHandler
    public void onInit(final FMLInitializationEvent evt) {
        if (AquaAcrobaticsCore.isLoaded()) {

            proxy.onInit();
        }
    }

    @Mod.EventHandler
    public void onPostInit(final FMLPostInitializationEvent evt) {

        if (AquaAcrobaticsCore.isLoaded()) {

            proxy.onPostInit();
        }
    }

}
