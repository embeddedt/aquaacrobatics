package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(Entity.class)
public abstract class EntityMixin {

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"))
    public boolean isSneaking(Entity entity) {

        // patches two calls to allow falling off blocks when not pressing sneak key but being in crouching pose
        if (entity instanceof IPlayerResizeable) {

            return ((IPlayerResizeable) entity).isActuallySneaking();
        }

        return this.isSneaking();
    }

    @Shadow
    public abstract boolean isSneaking();

}
