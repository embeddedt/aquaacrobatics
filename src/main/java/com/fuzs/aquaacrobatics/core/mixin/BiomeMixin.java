package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.biome.BiomeWaterFogColors;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Biome.class)
public abstract class BiomeMixin {
    @Shadow public abstract int getWaterColorMultiplier();

    /* For OptiFine */
    @SuppressWarnings("unused")
    public int aqua$waterColorMultiplier() {
        return BiomeWaterFogColors.getWaterColorForBiome((Biome)(Object)this, getWaterColorMultiplier());
    }
}
