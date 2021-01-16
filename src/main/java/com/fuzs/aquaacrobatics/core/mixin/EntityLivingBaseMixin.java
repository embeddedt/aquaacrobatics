package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity {

    public EntityLivingBaseMixin(World worldIn) {

        super(worldIn);
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isSneaking()Z"))
    public boolean isSneaking(EntityLivingBase entity) {

        // make sneaking on ladders work again since removing the pose client-side prevents the actual mechanic from working
        if (entity instanceof IPlayerResizeable) {

            return ((IPlayerResizeable) entity).isActuallySneaking();
        }

        return this.isSneaking();
    }

}
