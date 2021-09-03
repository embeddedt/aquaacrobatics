package com.fuzs.aquaacrobatics.biome;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.BiomeEvent;

import java.util.HashMap;

public abstract class BiomeWaterFogColors {
    private static final int DEFAULT_WATER_FOG_COLOR = 329011;
    private static final int DEFAULT_WATER_COLOR = 4159204;
    private static final HashMap<ResourceLocation, Integer> fogColorMap = new HashMap<>();
    private static final HashMap<ResourceLocation, Integer> baseColorMap = new HashMap<>();
    public static int getWaterFogColorForBiome(Biome biome) {
        ResourceLocation location = biome.getRegistryName();
        if(location == null)
            return DEFAULT_WATER_FOG_COLOR;
        Integer color = fogColorMap.get(location);
        if(color != null)
            return color;
        switch(location.toString()) {
            case "minecraft:mutated_swampland":
            case "minecraft:swampland":
                color = 2302743;
                break;
            case "integrateddynamics:biome_meneglin":
                color = 5613789;
                break;
            default:
                color = DEFAULT_WATER_FOG_COLOR;
                break;
        }
        fogColorMap.put(location, color);
        return color;
    }
    public static void getWaterColorForBiome(BiomeEvent.GetWaterColor event) {
        ResourceLocation location = event.getBiome().getRegistryName();
        if(location == null) {
            event.setNewColor(DEFAULT_WATER_COLOR);
            return;
        }
        Integer color = baseColorMap.get(location);
        if(color != null) {
            event.setNewColor(color);
            return;
        }
        switch(location.toString()) {
            case "minecraft:mutated_swampland":
            case "minecraft:swampland":
                color = 6388580;
                break;
            case "minecraft:frozen_river":
            case "minecraft:frozen_ocean": /* Frozen Ocean 1.13+ */    
                color = 3750089;
                break;
            case "minecraft:cold_beach": /* Snowy Beach 1.13+ */
            case "minecraft:taiga_cold": /* Snowy Taiga 1.13+ */
            case "minecraft:taiga_cold_hills": 
            case "minecraft:mutated_taiga_cold":
                color = 4020182;
                break;
            default:
                color = DEFAULT_WATER_COLOR; /* most mods won't be expecting 1.13 colors */
                break;
        }
        baseColorMap.put(location, color);
        event.setNewColor(color);
    }
}
