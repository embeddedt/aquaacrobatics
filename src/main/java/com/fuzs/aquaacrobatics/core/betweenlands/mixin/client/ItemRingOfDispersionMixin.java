package com.fuzs.aquaacrobatics.core.betweenlands.mixin.client;

import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thebetweenlands.common.item.equipment.ItemRingOfDispersion;

@Mixin(ItemRingOfDispersion.class)
public class ItemRingOfDispersionMixin {
    @Redirect(method = "canPhase", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;isSneaking()Z"))
    private boolean useTrueSneakState(EntityPlayer instance) {
        return ((IPlayerResizeable)instance).isActuallySneaking();
    }
}
