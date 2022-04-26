package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.biome.BiomeWaterFogColors;
import com.fuzs.aquaacrobatics.config.ConfigHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = { "net/minecraft/world/biome/BiomeColorHelper$3" })
public class BiomeMixin {
    /**
     * Typically we would just use the GetWaterColor event... but mods like Thaumcraft don't call it
     * and try to force their own water color on us.
     */
    @Inject(method = "func_180283_a", at = @At("RETURN"), cancellable = true, remap = false)
    @Dynamic("Exists only in an SRG environment")
    private void getNewWaterColorMultiplier(Biome biome, BlockPos position, CallbackInfoReturnable<Integer> cir) {
        if(ConfigHandler.BlocksConfig.newWaterColors)
            cir.setReturnValue(BiomeWaterFogColors.getWaterColorForBiome(biome, cir.getReturnValue()));
    }
}
