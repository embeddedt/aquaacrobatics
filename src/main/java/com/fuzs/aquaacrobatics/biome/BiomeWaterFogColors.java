package com.fuzs.aquaacrobatics.biome;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.BiomeEvent;

import java.util.HashMap;

public abstract class BiomeWaterFogColors {
    private static final int DEFAULT_WATER_FOG_COLOR = 329011;
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
            default:
                color = DEFAULT_WATER_FOG_COLOR;
                break;
        }
        fogColorMap.put(location, color);
        return color;
    }
    public static void getWaterColorForBiome(BiomeEvent.GetWaterColor event) {
        int color112 = event.getNewColor();
        ResourceLocation location = event.getBiome().getRegistryName();
        if(location == null) {
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
                if(location.getResourceDomain().equals("minecraft"))
                    color = 4159204;
                else
                    color = color112;
                break;
        }
        baseColorMap.put(location, color);
        event.setNewColor(color);
    }
}
