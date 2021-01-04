package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(NetHandlerPlayServer.class)
public class NetHandlerPlayServerMixin {

    @Redirect(method = "processEntityAction", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayerMP;motionY:D"))
    public double getElytraFlyingMotion(EntityPlayerMP player) {

        // 1.15 change for easier elytra takeoff
        return ConfigHandler.easyElytraTakeoff ? -1.0 : player.motionY;
    }

}
