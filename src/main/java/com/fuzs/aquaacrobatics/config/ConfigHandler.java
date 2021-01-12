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

    @Config.Name("Exact Player Collisions")
    @Config.Comment("Calculate player collisions with blocks exactly instead of merely estimating them based on full cubes as Minecraft 1.12 normally does. This is the default behavior in Minecraft 1.13+, but there might be a negative impact on client performance.")
    public static boolean exactPlayerCollisions = true;

    @Config.Name("Sideways Swimming")
    @Config.Comment("Enables swimming to both sides.")
    public static boolean sidewaysSwimming = false;

    @SuppressWarnings("unused")
    @Config.Name("integration")
    @Config.Comment("Control compatibility settings for individual mods.")
    public static IntegrationConfig integrationConfig;

    public static class IntegrationConfig {

        private static final String COMPAT_DESCRIPTION = "Only applies when the mod is installed. Disable when there are issues with the mod.";

        @Config.Name("Random Patches Integration")
        @Config.Comment(COMPAT_DESCRIPTION)
        public static boolean randomPatchesIntegration = true;

        @Config.Name("Mo' Bends Integration")
        @Config.Comment(COMPAT_DESCRIPTION)
        @Config.RequiresMcRestart
        public static boolean moBendsIntegration = true;

        @Config.Name("Wings Integration")
        @Config.Comment(COMPAT_DESCRIPTION)
        public static boolean wingsIntegration = true;

        @Config.Name("ArtemisLib Integration")
        @Config.Comment(COMPAT_DESCRIPTION)
        @Config.RequiresMcRestart
        public static boolean artemisLibIntegration = true;

        @Config.Name("Morph Integration")
        @Config.Comment(COMPAT_DESCRIPTION)
        public static boolean morphIntegration = true;

        @Config.Name("Hats Integration")
        @Config.Comment(COMPAT_DESCRIPTION)
        @Config.RequiresMcRestart
        public static boolean hatsIntegration = true;

        @Config.Name("Better Sprinting Integration")
        @Config.Comment(COMPAT_DESCRIPTION)
        public static boolean betterSprintingIntegration = true;

    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent evt) {

        if (evt.getModID().equals(AquaAcrobatics.MODID)) {

            ConfigManager.sync(AquaAcrobatics.MODID, Config.Type.INSTANCE);
        }
    }

}