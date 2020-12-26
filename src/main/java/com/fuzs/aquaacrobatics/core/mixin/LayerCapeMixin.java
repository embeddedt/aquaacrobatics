package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.entity.player.IPlayerSPSwimming;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(LayerCape.class)
public abstract class LayerCapeMixin {

    @Redirect(method = "doRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;isSneaking()Z"))
    public boolean shouldRenderSneakingDoRenderLayer(AbstractClientPlayer clientPlayer) {

        return ((IPlayerSPSwimming) clientPlayer).isCrouching();
    }

}
