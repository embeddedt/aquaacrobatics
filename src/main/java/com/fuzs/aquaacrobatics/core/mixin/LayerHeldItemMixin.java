package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.entity.player.IPlayerSPSwimming;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(LayerHeldItem.class)
public abstract class LayerHeldItemMixin {

    @Redirect(method = "renderHeldItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isSneaking()Z"))
    public boolean shouldRenderSneakingRenderHeldItem(EntityLivingBase entityIn) {

        if (entityIn instanceof IPlayerSPSwimming) {

            return ((IPlayerSPSwimming) entityIn).isCrouching();
        }
        else {

            return entityIn.isSneaking();
        }
    }

}
