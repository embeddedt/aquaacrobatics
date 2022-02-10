package com.fuzs.aquaacrobatics.core.mixin.client;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyArg(method = "renderWaterOverlayTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V", ordinal = 0), index = 3)
    private float replaceOpacity(float originalOpacity) {
        if(ConfigHandler.BlocksConfig.newWaterColors)
            return 0.1f;
        else
            return originalOpacity;
    }
}
