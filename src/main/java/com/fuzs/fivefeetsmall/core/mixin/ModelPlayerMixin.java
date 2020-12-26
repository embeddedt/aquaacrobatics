package com.fuzs.fivefeetsmall.core.mixin;

import com.fuzs.fivefeetsmall.entity.player.IModelPlayerSwimming;
import com.fuzs.fivefeetsmall.entity.player.IPlayerSPSwimming;
import com.fuzs.fivefeetsmall.entity.player.IPlayerSwimming;
import com.fuzs.fivefeetsmall.util.MathHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ModelPlayer.class)
public abstract class ModelPlayerMixin extends ModelBiped implements IModelPlayerSwimming {

    public float swimAnimation;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"))
    public boolean shouldRenderSneakingRender(Entity entityIn) {

        return ((IPlayerSPSwimming) entityIn).shouldRenderSneaking();
    }

//    @Redirect(method = "setRotationAngles", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"))
//    public boolean shouldRenderSneakingSetRotationAngles(Entity entityIn) {
//
//        return ((IPlayerSPSwimming) entityIn).shouldRenderSneaking();
//    }

//    @Inject(method = "setRotationAngles", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"), locals = LocalCapture.PRINT)
//    public void setRotationAngles111(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo callbackInfo) {
//
//        // return ((IPlayerSPSwimming) entityIn).shouldRenderSneaking();
//    }

    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {

        this.swimAnimation = ((IPlayerSwimming) entitylivingbaseIn).getSwimAnimation(partialTickTime);
    }

    @ModifyArg(method = "setRotationAngles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBiped;setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V"), index = 4)
    public float getHeadPitch(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {

        boolean flag = entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getTicksElytraFlying() > 4;
        boolean flag1 = ((IPlayerSwimming) entityIn).isActuallySwimming();
        if (!flag && this.swimAnimation > 0.0F) {

            if (flag1) {

                return this.rotLerpRad(this.bipedHead.rotateAngleX, (-(float)Math.PI / 4F), this.swimAnimation) / 0.017453292F;
            } else {

                return this.rotLerpRad(this.bipedHead.rotateAngleX, headPitch * ((float)Math.PI / 180F), this.swimAnimation) / 0.017453292F;
            }
        }

        return headPitch;
    }

    @Inject(method = "setRotationAngles", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/model/ModelBiped;setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V"))
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo callbackInfo) {

        if (this.swimAnimation > 0.0F) {

            float f7 = limbSwing % 26.0F;
            float f8 = this.swingProgress > 0.0F ? 0.0F : this.swimAnimation;
            if (f7 < 14.0F) {

                this.bipedLeftArm.rotateAngleX = this.rotLerpRad(this.bipedLeftArm.rotateAngleX, 0.0F, this.swimAnimation);
                this.bipedRightArm.rotateAngleX = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleX, 0.0F);
                this.bipedLeftArm.rotateAngleY = this.rotLerpRad(this.bipedLeftArm.rotateAngleY, (float)Math.PI, this.swimAnimation);
                this.bipedRightArm.rotateAngleY = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleY, (float)Math.PI);
                this.bipedLeftArm.rotateAngleZ = this.rotLerpRad(this.bipedLeftArm.rotateAngleZ, (float)Math.PI + 1.8707964F * this.getArmAngleSq(f7) / this.getArmAngleSq(14.0F), this.swimAnimation);
                this.bipedRightArm.rotateAngleZ = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleZ, (float)Math.PI - 1.8707964F * this.getArmAngleSq(f7) / this.getArmAngleSq(14.0F));
            } else if (f7 >= 14.0F && f7 < 22.0F) {

                float f10 = (f7 - 14.0F) / 8.0F;
                this.bipedLeftArm.rotateAngleX = this.rotLerpRad(this.bipedLeftArm.rotateAngleX, ((float)Math.PI / 2F) * f10, this.swimAnimation);
                this.bipedRightArm.rotateAngleX = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleX, ((float)Math.PI / 2F) * f10);
                this.bipedLeftArm.rotateAngleY = this.rotLerpRad(this.bipedLeftArm.rotateAngleY, (float)Math.PI, this.swimAnimation);
                this.bipedRightArm.rotateAngleY = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleY, (float)Math.PI);
                this.bipedLeftArm.rotateAngleZ = this.rotLerpRad(this.bipedLeftArm.rotateAngleZ, 5.012389F - 1.8707964F * f10, this.swimAnimation);
                this.bipedRightArm.rotateAngleZ = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleZ, 1.2707963F + 1.8707964F * f10);
            } else if (f7 >= 22.0F && f7 < 26.0F) {

                float f9 = (f7 - 22.0F) / 4.0F;
                this.bipedLeftArm.rotateAngleX = this.rotLerpRad(this.bipedLeftArm.rotateAngleX, ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f9, this.swimAnimation);
                this.bipedRightArm.rotateAngleX = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleX, ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f9);
                this.bipedLeftArm.rotateAngleY = this.rotLerpRad(this.bipedLeftArm.rotateAngleY, (float)Math.PI, this.swimAnimation);
                this.bipedRightArm.rotateAngleY = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleY, (float)Math.PI);
                this.bipedLeftArm.rotateAngleZ = this.rotLerpRad(this.bipedLeftArm.rotateAngleZ, (float)Math.PI, this.swimAnimation);
                this.bipedRightArm.rotateAngleZ = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleZ, (float)Math.PI);
            }

            this.bipedLeftLeg.rotateAngleX = MathHelper.lerp(this.swimAnimation, this.bipedLeftLeg.rotateAngleX, 0.3F * MathHelper.cos(limbSwing * 0.33333334F + (float)Math.PI));
            this.bipedRightLeg.rotateAngleX = MathHelper.lerp(this.swimAnimation, this.bipedRightLeg.rotateAngleX, 0.3F * MathHelper.cos(limbSwing * 0.33333334F));
        }

        copyModelAngles(this.bipedHead, this.bipedHeadwear);
    }

    private float getArmAngleSq(float limbSwing) {
        
        return -65.0F * limbSwing + limbSwing * limbSwing;
    }

    private float rotLerpRad(float angleIn, float maxAngleIn, float mulIn) {

        float f = (maxAngleIn - angleIn) % ((float)Math.PI * 2F);
        if (f < -(float)Math.PI) {

            f += ((float)Math.PI * 2F);
        }

        if (f >= (float)Math.PI) {

            f -= ((float)Math.PI * 2F);
        }

        return angleIn + mulIn * f;
    }

    @Override
    public void setSwimAnimation(float swimAnimation) {

        this.swimAnimation = swimAnimation;
    }

}
