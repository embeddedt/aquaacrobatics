package com.fuzs.aquaacrobatics.core.mixin.client;

import com.fuzs.aquaacrobatics.client.entity.IPlayerSPSwimming;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(PlayerControllerMP.class)
public abstract class PlayerControllerMPMixin {

    @Redirect(method = "processRightClickBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isSneaking()Z"))
    public boolean isSneaking(EntityPlayerSP playerIn) {

        return ((IPlayerSPSwimming) playerIn).isActuallySneaking();
    }

}
