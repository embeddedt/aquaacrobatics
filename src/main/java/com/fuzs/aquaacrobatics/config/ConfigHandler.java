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

    @Config.Name("Enable Random Patches Compat")
    @Config.Comment("Obviously only applies when the mod is installed. Disable when there are issues with the mod.")
    public static boolean randomPatchesCompat = true;

    @Config.Name("Enable Mo' Bends Compat")
    @Config.Comment("Obviously only applies when the mod is installed. Disable when there are issues with the mod.")
    public static boolean moBendsCompat = true;

    @Config.Name("Enable Obfuscate Compat")
    @Config.Comment("Obviously only applies when the mod is installed. Disable when there are issues with the mod.")
    public static boolean obfuscateCompat = true;

    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent evt) {

        if (evt.getModID().equals(AquaAcrobatics.MODID)) {

            ConfigManager.sync(AquaAcrobatics.MODID, Config.Type.INSTANCE);
        }
    }

}