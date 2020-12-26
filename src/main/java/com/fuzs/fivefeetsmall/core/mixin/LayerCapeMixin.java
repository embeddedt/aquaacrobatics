package com.fuzs.fivefeetsmall.core.mixin;

import com.fuzs.fivefeetsmall.entity.player.IPlayerSPSwimming;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LayerCape.class)
public abstract class LayerCapeMixin {

    @Redirect(method = "doRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;isSneaking()Z"))
    public boolean shouldRenderSneakingDoRenderLayer(AbstractClientPlayer clientPlayer) {

        return ((IPlayerSPSwimming) clientPlayer).shouldRenderSneaking();
    }

}
