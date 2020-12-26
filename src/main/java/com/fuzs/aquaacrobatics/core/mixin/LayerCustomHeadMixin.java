package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.entity.player.IPlayerSPSwimming;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(LayerCustomHead.class)
public abstract class LayerCustomHeadMixin {

    @Redirect(method = "doRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isSneaking()Z"))
    public boolean shouldRenderSneakingDoRenderLayer(EntityLivingBase entityIn) {

        if (entityIn instanceof IPlayerSPSwimming) {

            return ((IPlayerSPSwimming) entityIn).isCrouching();
        }
        else {

            return entityIn.isSneaking();
        }
    }

}
