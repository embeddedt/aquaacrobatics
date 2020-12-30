package com.fuzs.aquaacrobatics;

import com.fuzs.aquaacrobatics.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
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
        dependencies = "before:mobends@(0.24,)"
)
public class AquaAcrobatics {

    public static final String MODID = "aquaacrobatics";
    public static final String NAME = "Aqua Acrobatics";
    public static final String VERSION = "1.1.6";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    private static final String CLIENT_PROXY = "com.fuzs." + MODID + ".proxy.ClientProxy";
    private static final String COMMON_PROXY = "com.fuzs." + MODID + ".proxy.CommonProxy";

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    private static CommonProxy proxy;

    @Mod.EventHandler
    public void onPreInit(final FMLPreInitializationEvent evt) {

        proxy.onPreInit();
    }

    @Mod.EventHandler
    public void onPostInit(final FMLPostInitializationEvent evt) {

        proxy.onPostInit();
    }

}
