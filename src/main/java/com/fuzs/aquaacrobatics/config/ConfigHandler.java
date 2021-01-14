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

    @Config.Name("Exact Player Collisions")
    @Config.Comment("Calculate player collisions with blocks exactly instead of merely estimating them based on full cubes as Minecraft 1.12 normally does. This is the default behavior in Minecraft 1.13+, but there might be a negative impact on client performance.")
    public static boolean exactPlayerCollisions = false;

    @SuppressWarnings("unused")
    @Config.Name("movement")
    @Config.Comment("Movement related config options.")
    public static MovementConfig movementConfig;

    @SuppressWarnings("unused")
    @Config.Name("miscellaneous")
    @Config.Comment("Config options for various features of the mod.")
    public static MiscellaneousConfig miscellaneousConfig;

    @SuppressWarnings("unused")
    @Config.Name("integration")
    @Config.Comment("Control compatibility settings for individual mods.")
    public static IntegrationConfig integrationConfig;

    public static class MovementConfig {

        @Config.Name("No Double Tab Sprinting")
        @Config.Comment("Prevent sprinting from being triggered by double tapping the walk forward key.")
        public static boolean noDoubleTapSprinting = false;

        @Config.Name("Sideways Sprinting")
        @Config.Comment("Enables sprinting to the left and right.")
        public static boolean sidewaysSprinting = false;

        @Config.Name("Sideways Swimming")
        @Config.Comment("Enables swimming to the left and right.")
        public static boolean sidewaysSwimming = false;

    }

    public static class MiscellaneousConfig {

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

    }

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

    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent evt) {

        if (evt.getModID().equals(AquaAcrobatics.MODID)) {

            ConfigManager.sync(AquaAcrobatics.MODID, Config.Type.INSTANCE);
        }
    }

}