package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.entity.IBubbleColumnInteractable;
import com.fuzs.aquaacrobatics.entity.IRockableBoat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityBoat.class)
public abstract class EntityBoatMixin extends Entity implements IBubbleColumnInteractable, IRockableBoat {
    private static final DataParameter<Integer> ROCKING_TICKS = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);

    private boolean aqua$rocking;
    private boolean aqua$rockingDownwards;
    private float rockingIntensity;
    private float rockingAngle;
    private float prevRockingAngle;
    
    public EntityBoatMixin(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "entityInit", at = @At("TAIL"))
    private void registerRockingData(CallbackInfo ci) {
        if(ConfigHandler.MiscellaneousConfig.bubbleColumns)
            this.dataManager.register(ROCKING_TICKS, 0);
    }
    
    public void onEnterBubbleColumnWithAirAbove(boolean downwards) {
        if (!world.isRemote) {
            this.aqua$rocking = true;
            this.aqua$rockingDownwards = downwards;
            if (this.getRockingTicks() == 0) {
                this.setRockingTicks(60);
            }
        }

        this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double)this.rand.nextFloat(), this.posY + 0.7D, this.posZ + (double)this.rand.nextFloat(), 0.0D, 0.0D, 0.0D);
        if (this.rand.nextInt(20) == 0) {
            this.world.playSound(this.posX, this.posY, this.posZ, this.getSplashSound(), this.getSoundCategory(), 1.0F, 0.8F + 0.4F * this.rand.nextFloat(), false);
        }
    }
    
    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/item/EntityBoat;doBlockCollisions()V"))
    private void updateRocking(CallbackInfo ci) {
        if (this.world.isRemote) {
            int i = this.getRockingTicks();
            if (i > 0) {
                this.rockingIntensity += 0.05F;
            } else {
                this.rockingIntensity -= 0.1F;
            }

            this.rockingIntensity = MathHelper.clamp(this.rockingIntensity, 0.0F, 1.0F);
            this.prevRockingAngle = this.rockingAngle;
            this.rockingAngle = 10.0F * (float)Math.sin((double)(0.5F * (float)this.world.getTotalWorldTime())) * this.rockingIntensity;
        } else {
            if (!this.aqua$rocking) {
                this.setRockingTicks(0);
            }

            int k = this.getRockingTicks();
            if (k > 0) {
                --k;
                this.setRockingTicks(k);
                int j = 60 - k - 1;
                if (j > 0 && k == 0) {
                    this.setRockingTicks(0);
                    if (this.aqua$rockingDownwards) {
                        this.motionY -= 0.7D;
                        this.removePassengers();
                    } else {
                        this.motionY = this.aqua$isPlayerRiding() ? 2.7D : 0.6D;
                    }
                }

                this.aqua$rocking = false;
            }
        }

    }
    
    private boolean aqua$isPlayerRiding() {
        for(Entity entity : this.getPassengers()) {
            if (EntityPlayer.class.isAssignableFrom(entity.getClass())) {
                return true;
            }
        }

        return false;
    }

    public void setRockingTicks(int p_203055_1_) {
        if(!ConfigHandler.MiscellaneousConfig.bubbleColumns)
            return;
        this.dataManager.set(ROCKING_TICKS, p_203055_1_);
    }

    public int getRockingTicks() {
        if(!ConfigHandler.MiscellaneousConfig.bubbleColumns)
            return 0;
        return this.dataManager.get(ROCKING_TICKS);
    }

    @SideOnly(Side.CLIENT)
    public float getRockingAngle(float partialTicks) {
        if(!ConfigHandler.MiscellaneousConfig.bubbleColumns)
            return 0.0f;
        return this.prevRockingAngle + (this.rockingAngle - this.prevRockingAngle) * partialTicks;
    }

}
