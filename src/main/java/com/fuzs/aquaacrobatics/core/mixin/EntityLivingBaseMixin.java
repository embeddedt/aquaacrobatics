package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import com.fuzs.aquaacrobatics.proxy.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
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
    
    private boolean aqua$isLosingAir() {
        if(ConfigHandler.MiscellaneousConfig.bubbleColumns
                && this.world.getBlockState(new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ)).getBlock() == CommonProxy.BUBBLE_COLUMN)
            return false; /* pretend not to be in water */
        return this.isInsideOfMaterial(Material.WATER);
    }

    @Redirect(method = "onEntityUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isInsideOfMaterial(Lnet/minecraft/block/material/Material;)Z"))
    private boolean checkBubbleBreathing(EntityLivingBase entityLivingBase, Material materialIn) {
        if(materialIn == Material.WATER)
            return aqua$isLosingAir();
        return entityLivingBase.isInsideOfMaterial(materialIn);
    }
    
    @ModifyArg(method = "onEntityUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;setAir(I)V"), index = 0)
    private int getNewAirValue(int original) {
        if(ConfigHandler.MiscellaneousConfig.slowAirReplenish && original == 300 && this.getAir() >= -20 && !aqua$isLosingAir()) {
            int oldAirValue = Math.max(this.getAir(), 0);
            return Math.min(oldAirValue + 4, 300);
        }
        return original;
    }
}
