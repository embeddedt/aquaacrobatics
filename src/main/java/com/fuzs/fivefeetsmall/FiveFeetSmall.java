package com.fuzs.fivefeetsmall;

import com.fuzs.fivefeetsmall.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = FiveFeetSmall.MODID,
        name = FiveFeetSmall.NAME,
        version = FiveFeetSmall.VERSION,
        acceptedMinecraftVersions = FiveFeetSmall.RANGE,
        certificateFingerprint = FiveFeetSmall.FINGERPRINT,
        dependencies = FiveFeetSmall.DEPENDENCIES
)
public class FiveFeetSmall
{
    public static final String MODID = "fivefeetsmall";
    public static final String NAME = "Five Feet Small";
    public static final String VERSION = "@VERSION@";
    public static final String RANGE = "[1.12.2]";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2779,)";
    public static final String FINGERPRINT = "@FINGERPRINT@";

    public static final String CLIENT_PROXY_CLASS = "com.fuzs.fivefeetsmall.proxy.ClientProxy";
    public static final String SERVER_PROXY_CLASS = "com.fuzs.fivefeetsmall.proxy.ServerProxy";

    public static final Logger LOGGER = LogManager.getLogger(FiveFeetSmall.NAME);

    @SidedProxy(clientSide = FiveFeetSmall.CLIENT_PROXY_CLASS, serverSide = FiveFeetSmall.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @EventHandler
    public void fingerprintViolation(FMLFingerprintViolationEvent event) {
        LOGGER.warn("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }
}
