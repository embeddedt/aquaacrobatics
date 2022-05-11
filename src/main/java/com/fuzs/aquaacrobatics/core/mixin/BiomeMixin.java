package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.biome.BiomeWaterFogColors;
import com.fuzs.aquaacrobatics.config.ConfigHandler;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class BiomeMixin {
    @Shadow public abstract int getWaterColorMultiplier();

    /* For OptiFine */
    @SuppressWarnings("unused")
    public int aqua$waterColorMultiplier() {
        if(ConfigHandler.BlocksConfig.newWaterColors) {
            /* We might call getWaterColorForBiome twice, but it's fine because it caches after the first call */
            return BiomeWaterFogColors.getWaterColorForBiome((Biome) (Object) this, getWaterColorMultiplier());
        } else
            return getWaterColorMultiplier();
    }

    @Inject(method = "getWaterColorMultiplier", at = @At("TAIL"), remap = false, cancellable = true)
    private void forceNewColor(CallbackInfoReturnable<Integer> cir) {
        if(ConfigHandler.BlocksConfig.newWaterColors) {
            cir.setReturnValue(BiomeWaterFogColors.getWaterColorForBiome((Biome)(Object)this, cir.getReturnValue()));
        }
    }
}
