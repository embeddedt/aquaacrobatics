package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.biome.BiomeWaterFogColors;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public class BiomeMixin {
    /**
     * Typically we would just use the GetWaterColor event... but mods like Thaumcraft don't call it
     * and try to force their own water color on us.
     */
    @Inject(method = "getWaterColorMultiplier", at = @At("RETURN"), cancellable = true, remap = false)
    private void getNewWaterColorMultiplier(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(BiomeWaterFogColors.getWaterColorForBiome((Biome)(Object)this, cir.getReturnValue()));
    }
}
