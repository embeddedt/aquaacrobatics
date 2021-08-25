package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Allows items to float like post-1.13.
 */
@Mixin(EntityItem.class)
public abstract class EntityItemMixin extends Entity {
    public EntityItemMixin(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    private void applyFloatMotion() {
        //System.out.println("APPLY FLOAT");
        if (this.motionY < (double)0.06F) {
            this.motionY += (double)5.0E-4F;
        }
        this.motionX *= 0.99F;
        this.motionZ *= 0.99F;
    }
    
    @Redirect(method = "onUpdate", at = @At(value="INVOKE", target = "Lnet/minecraft/entity/item/EntityItem;hasNoGravity()Z", ordinal = 0))
    private boolean applyFloatMotionIfInWater(EntityItem entityItem) {
        if(this.world.getBlockState(new BlockPos(entityItem)).getMaterial() == Material.WATER) {
            applyFloatMotion();
            return true;
        } else {
            return entityItem.hasNoGravity();
        }
    }
}
