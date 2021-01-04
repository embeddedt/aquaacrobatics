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

    @Config.Name("Easy Elytra Takeoff")
    @Config.Comment("Taking off with an elytra from the ground is now far easier like in Minecraft 1.15+.")
    public static boolean easyElytraTakeoff = true;

    @Config.Name("Sneaking Dismounts Parrots")
    @Config.Comment("Parrots no longer leave the players shoulders as easily, instead the player needs to press the sneak key.")
    public static boolean sneakingForParrots = true;

    @Config.Name("Eating Animation")
    @Config.Comment("Animate eating in third-person view.")
    public static boolean eatingAnimation = true;

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

        @Config.Name("Wings Compat")
        @Config.Comment(COMPAT_DESCRIPTION)
        public static boolean wingsCompat = true;

        @Config.Name("ArtemisLib Compat")
        @Config.Comment(COMPAT_DESCRIPTION)
        @Config.RequiresMcRestart
        public static boolean artemisLibCompat = true;

        @Config.Name("ArtemisLib Fix Player Model")
        @Config.Comment("Fix player model having wrong horizontal scale while elytra flying and swimming. Incompatible with a lot of mods that touch player rendering.")
        @Config.RequiresMcRestart
        public static boolean artemisLibFixModel = false;

        @Config.Name("Morph Compat")
        @Config.Comment(COMPAT_DESCRIPTION)
        public static boolean morphCompat = true;
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent evt) {

        if (evt.getModID().equals(AquaAcrobatics.MODID)) {

            ConfigManager.sync(AquaAcrobatics.MODID, Config.Type.INSTANCE);
        }
    }

}