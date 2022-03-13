package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.entity.IBubbleColumnInteractable;
import com.fuzs.aquaacrobatics.entity.IEntitySwimmer;
import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(Entity.class)
public abstract class EntityMixin implements IBubbleColumnInteractable {
    @Shadow
    public abstract boolean isSneaking();

    @Shadow
    public double motionY;

    @Shadow
    public float fallDistance;

    @Shadow public World world;

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"))
    public boolean isSneaking(Entity entity) {

        // patches two calls to allow falling off blocks when not pressing sneak key but being in crouching pose
        if (entity instanceof IPlayerResizeable) {

            return ((IPlayerResizeable) entity).isActuallySneaking();
        }

        return this.isSneaking();
    }

    @Inject(method = "onEntityUpdate", at = @At("RETURN"))
    private void handleUpdateSwimming(CallbackInfo ci) {
        if(this instanceof IEntitySwimmer) {
            ((IEntitySwimmer)(Object)this).updateSwimming();
        }
    }

    @Override
    public void onEnterBubbleColumn(boolean downwards) {
        if(!downwards) {
            this.motionY = Math.min(0.7, this.motionY + 0.06);
        } else
            this.motionY = Math.max(-0.3, this.motionY - 0.03);
        this.fallDistance = 0.0F;
    }

    @Override
    public void onEnterBubbleColumnWithAirAbove(boolean downwards) {
        if(!downwards) {
            this.motionY = Math.min(1.8, this.motionY + 0.1);
        } else
            this.motionY = Math.max(-0.9, this.motionY - 0.03);
    }
}
