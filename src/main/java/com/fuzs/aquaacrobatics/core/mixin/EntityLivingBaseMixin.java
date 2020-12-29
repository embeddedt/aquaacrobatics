package com.fuzs.aquaacrobatics.core.mixin;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity {

    @Shadow
    public float prevLimbSwingAmount;
    @Shadow
    public float limbSwingAmount;
    @Shadow
    public float limbSwing;

    public EntityLivingBaseMixin(World worldIn) {

        super(worldIn);
    }

    @Shadow
    public abstract boolean isServerWorld();

    @Shadow
    public abstract float getAIMoveSpeed();

    @Shadow
    public abstract boolean isOnLadder();

    @Shadow
    protected abstract float getWaterSlowDown();

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getWaterSlowDown()F"))
    public float getSwimmingSlowDown(EntityLivingBase entityIn) {

        return this.isSprinting() ? 0.9F : this.getWaterSlowDown();
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(float strafe, float vertical, float forward, CallbackInfo callbackInfo) {

        if (this.isServerWorld() || this.canPassengerSteer()) {

            if (!((EntityLivingBase) (Object) this instanceof EntityPlayer) || !((EntityPlayer) (Object) this).capabilities.isFlying) {

                if (this.isInWater()) {

                    double d8 = this.posY;
                    float f5 = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
                    float f6 = 0.02F;
                    float f7 = (float) EnchantmentHelper.getDepthStriderModifier((EntityLivingBase) (Object) this);
                    if (f7 > 3.0F) {

                        f7 = 3.0F;
                    }

                    if (!this.onGround) {

                        f7 *= 0.5F;
                    }

                    if (f7 > 0.0F) {

                        f5 += (0.54600006F - f5) * f7 / 3.0F;
                        f6 += (this.getAIMoveSpeed() - f6) * f7 / 3.0F;
                    }

                    this.moveRelative(strafe, vertical, forward, f6);
                    this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                    if (this.collidedHorizontally && this.isOnLadder()) {

                        this.motionY = 0.2;
                    }

                    this.motionX *= f5;
                    this.motionY *= 0.8;
                    this.motionZ *= f5;
                    if (!this.hasNoGravity() && !this.isSprinting()) {

                        if (this.motionY < 0.0 && Math.abs(this.motionY - 0.005) >= 0.003 && Math.abs(this.motionY - 0.08 / 16.0) < 0.003D) {

                            this.motionY = -0.003;
                        } else {

                            this.motionY = this.motionY - 0.08 / 16.0;
                        }
                    }

                    if (this.collidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6 - this.posY + d8, this.motionZ)) {

                        this.motionY = 0.3;
                    }

                    this.updateLimbSwing();
                    callbackInfo.cancel();
                } else if (this.isInLava()) {

                    double d7 = this.posY;
//                    this.moveRelative(strafe, vertical, forward, 0.02F);
//                    this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
//                    if (this.func_233571_b_(FluidTags.LAVA) <= this.func_233579_cu_()) {
//                        this.setMotion(this.getMotion().mul(0.5D, (double)0.8F, 0.5D));
//                        Vector3d vector3d3 = this.func_233626_a_(d0, flag, this.getMotion());
//                        this.setMotion(vector3d3);
//                    } else {
//                        this.setMotion(this.getMotion().scale(0.5D));
//                    }
//
//                    if (!this.hasNoGravity()) {
//                        this.setMotion(this.getMotion().add(0.0D, -d0 / 4.0D, 0.0D));
//                    }
//
//                    Vector3d vector3d4 = this.getMotion();
//                    if (this.collidedHorizontally && this.isOffsetPositionInLiquid(vector3d4.x, vector3d4.y + (double)0.6F - this.getPosY() + d7, vector3d4.z)) {
//                        this.setMotion(vector3d4.x, (double)0.3F, vector3d4.z);
//                    }

                    this.updateLimbSwing();
                    callbackInfo.cancel();
                }
            }
        }
    }

    private void updateLimbSwing() {

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d5 = this.posX - this.prevPosX;
        double d7 = this.posZ - this.prevPosZ;
        double d9 = this instanceof EntityFlying ? this.posY - this.prevPosY : 0.0;
        float f10 = MathHelper.sqrt(d5 * d5 + d9 * d9 + d7 * d7) * 4.0F;

        if (f10 > 1.0F) {

            f10 = 1.0F;
        }

        this.limbSwingAmount += (f10 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

}
