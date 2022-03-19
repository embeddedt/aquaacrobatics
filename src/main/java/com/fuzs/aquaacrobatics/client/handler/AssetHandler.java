package com.fuzs.aquaacrobatics.client.handler;

import com.cleanroommc.assetmover.AssetMoverAPI;
import com.fuzs.aquaacrobatics.AquaAcrobatics;

import java.util.HashMap;
import java.util.Map;

public class AssetHandler {
    private static Map<String, String> assetPaths = new HashMap<>();

    private static void putTexture(String assetPath) {
        assetPaths.put("assets/minecraft/" + assetPath, "assets/aquaacrobatics/" + assetPath);
    }

    private static void putStructure(String assetPath) {
        assetPaths.put("data/minecraft/" + assetPath, "assets/aquaacrobatics/" + assetPath);
    }

    public static void downloadAssets() {
        AquaAcrobatics.LOGGER.info("Aqua Acrobatics is loading vanilla assets from newer versions...");
        putTexture("textures/block/brain_coral.png");
        putTexture("textures/block/brain_coral_block.png");
        putTexture("textures/block/brain_coral_fan.png");
        putTexture("textures/block/bubble_coral.png");
        putTexture("textures/block/bubble_coral_block.png");
        putTexture("textures/block/bubble_coral_fan.png");
        putTexture("textures/block/dead_brain_coral.png");
        putTexture("textures/block/dead_brain_coral_block.png");
        putTexture("textures/block/dead_brain_coral_fan.png");
        putTexture("textures/block/dead_bubble_coral.png");
        putTexture("textures/block/dead_bubble_coral_block.png");
        putTexture("textures/block/dead_bubble_coral_fan.png");
        putTexture("textures/block/dead_fire_coral.png");
        putTexture("textures/block/dead_fire_coral_block.png");
        putTexture("textures/block/dead_fire_coral_fan.png");
        putTexture("textures/block/dead_horn_coral.png");
        putTexture("textures/block/dead_horn_coral_block.png");
        putTexture("textures/block/dead_horn_coral_fan.png");
        putTexture("textures/block/dead_tube_coral.png");
        putTexture("textures/block/dead_tube_coral_block.png");
        putTexture("textures/block/dead_tube_coral_fan.png");
        putTexture("textures/block/fire_coral.png");
        putTexture("textures/block/fire_coral_block.png");
        putTexture("textures/block/fire_coral_fan.png");
        putTexture("textures/block/horn_coral.png");
        putTexture("textures/block/horn_coral_block.png");
        putTexture("textures/block/horn_coral_fan.png");
        putTexture("textures/block/tube_coral.png");
        putTexture("textures/block/tube_coral_block.png");
        putTexture("textures/block/tube_coral_fan.png");

        putTexture("textures/block/dried_kelp_bottom.png");
        putTexture("textures/block/dried_kelp_side.png");
        putTexture("textures/block/dried_kelp_top.png");
        putTexture("textures/block/kelp.png");
        putTexture("textures/block/kelp.png.mcmeta");
        putTexture("textures/block/kelp_plant.png");
        putTexture("textures/block/kelp_plant.png.mcmeta");
        putTexture("textures/item/dried_kelp.png");
        putTexture("textures/item/kelp.png");

        putTexture("textures/block/seagrass.png");
        putTexture("textures/block/seagrass.png.mcmeta");
        putTexture("textures/block/tall_seagrass_bottom.png");
        putTexture("textures/block/tall_seagrass_bottom.png.mcmeta");
        putTexture("textures/block/tall_seagrass_top.png");
        putTexture("textures/block/tall_seagrass_top.png.mcmeta");
        putTexture("textures/item/seagrass.png");

        putTexture("textures/block/conduit.png");

        putTexture("textures/item/heart_of_the_sea.png");
        putTexture("textures/item/nautilus_shell.png");

        putTexture("textures/entity/conduit/base.png");
        putTexture("textures/entity/conduit/break_particle.png");
        putTexture("textures/entity/conduit/cage.png");
        putTexture("textures/entity/conduit/closed_eye.png");
        putTexture("textures/entity/conduit/open_eye.png");
        putTexture("textures/entity/conduit/wind.png");
        putTexture("textures/entity/conduit/wind_vertical.png");
        putTexture("textures/entity/zombie/drowned.png");
        putTexture("textures/entity/zombie/drowned_outer_layer.png");

        AssetMoverAPI.fromMinecraft("1.18.1", assetPaths);
        assetPaths = null; /* allow GC */
    }
}
