package com.fuzs.aquaacrobatics.config;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = AquaAcrobatics.MODID)
@Mod.EventBusSubscriber
public class ConfigHandler {

    @Config.Name("Replenish Air Slowly")
    @Config.Comment("Replenish air slowly when out of water instead of immediately.")
    public static boolean slowAirReplenish = false;

    @SuppressWarnings("unused")
    @Config.Name("compat")
    @Config.Comment("Control compatibility settings for individual mods.")
    public static CompatConfig compatConfig;

    public static class CompatConfig {

        private static final String COMPAT_DESCRIPTION = "Only applies when the mod is installed. Disable when there are issues with the mod.";

        @Config.Name("Random Patches Compat")
        @Config.Comment(COMPAT_DESCRIPTION)
        public static boolean randomPatchesCompat = true;

        @Config.Name("Mo' Bends Compat")
        @Config.Comment(COMPAT_DESCRIPTION)
        public static boolean moBendsCompat = true;

        @Config.Name("Obfuscate Compat")
        @Config.Comment(COMPAT_DESCRIPTION)
        public static boolean obfuscateCompat = true;

        @Config.Name("Wings Compat")
        @Config.Comment(COMPAT_DESCRIPTION)
        public static boolean wingsCompat = true;
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent evt) {

        if (evt.getModID().equals(AquaAcrobatics.MODID)) {

            ConfigManager.sync(AquaAcrobatics.MODID, Config.Type.INSTANCE);
        }
    }

}