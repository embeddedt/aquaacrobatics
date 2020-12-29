package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.entity.player.IModelBipedSwimming;
import com.fuzs.aquaacrobatics.entity.player.IPlayerSPSwimming;
import com.fuzs.aquaacrobatics.entity.player.IPlayerSwimming;
import com.fuzs.aquaacrobatics.util.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
@Mixin(ModelBiped.class)
public abstract class ModelBipedMixin extends ModelBase implements IModelBipedSwimming {

    @Shadow
    public ModelRenderer bipedHead;
    @Shadow
    public ModelRenderer bipedRightArm;
    @Shadow
    public ModelRenderer bipedLeftArm;
    @Shadow
    public ModelRenderer bipedRightLeg;
    @Shadow
    public ModelRenderer bipedLeftLeg;
    
    public float swimAnimation;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"))
    public boolean shouldRenderSneakingRender(Entity entityIn) {

        if (entityIn instanceof IPlayerSPSwimming) {

            return ((IPlayerSPSwimming) entityIn).isCrouching();
        }
         else {

             return entityIn.isSneaking();
        }
    }

    @ModifyVariable(method = "setRotationAngles", at = @At("HEAD"), ordinal = 4, argsOnly = true)
    public float getHeadPitch(float f, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {

        if (entityIn instanceof IPlayerSwimming) {

            boolean flag = ((EntityLivingBase) entityIn).getTicksElytraFlying() > 4;
            boolean flag1 = ((IPlayerSwimming) entityIn).isActuallySwimming();
            if (!flag && this.swimAnimation > 0.0F) {

                if (flag1) {

                    return this.rotLerpRad(this.swimAnimation, this.bipedHead.rotateAngleX, ((float) -Math.PI / 4F)) / 0.017453292F;
                } else {

                    return this.rotLerpRad(this.swimAnimation, this.bipedHead.rotateAngleX, headPitch * ((float) Math.PI / 180F)) / 0.017453292F;
                }
            }
        }

        return headPitch;
    }

    @Inject(method = "setRotationAngles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBiped;copyModelAngles(Lnet/minecraft/client/model/ModelRenderer;Lnet/minecraft/client/model/ModelRenderer;)V"))
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo callbackInfo) {

        if (this.swimAnimation > 0.0F) {

            float f1 = limbSwing % 26.0F;
            EnumHandSide handside = this.getMainHand(entityIn);
            float f2 = handside == EnumHandSide.RIGHT && this.swingProgress > 0.0F ? 0.0F : this.swimAnimation;
            float f3 = handside == EnumHandSide.LEFT && this.swingProgress > 0.0F ? 0.0F : this.swimAnimation;
            if (f1 < 14.0F) {

                this.bipedLeftArm.rotateAngleX = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleX, 0.0F);
                this.bipedRightArm.rotateAngleX = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleX, 0.0F);
                this.bipedLeftArm.rotateAngleY = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleY, (float) Math.PI);
                this.bipedRightArm.rotateAngleY = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleY, (float) Math.PI);
                this.bipedLeftArm.rotateAngleZ = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleZ, (float) Math.PI + 1.8707964F * this.getArmAngleSq(f1) / this.getArmAngleSq(14.0F));
                this.bipedRightArm.rotateAngleZ = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleZ, (float) Math.PI - 1.8707964F * this.getArmAngleSq(f1) / this.getArmAngleSq(14.0F));
            } else if (f1 >= 14.0F && f1 < 22.0F) {

                float f10 = (f1 - 14.0F) / 8.0F;
                this.bipedLeftArm.rotateAngleX = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleX, ((float) Math.PI / 2F) * f10);
                this.bipedRightArm.rotateAngleX = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleX, ((float) Math.PI / 2F) * f10);
                this.bipedLeftArm.rotateAngleY = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleY, (float) Math.PI);
                this.bipedRightArm.rotateAngleY = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleY, (float) Math.PI);
                this.bipedLeftArm.rotateAngleZ = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleZ, 5.012389F - 1.8707964F * f10);
                this.bipedRightArm.rotateAngleZ = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleZ, 1.2707963F + 1.8707964F * f10);
            } else if (f1 >= 22.0F && f1 < 26.0F) {

                float f9 = (f1 - 22.0F) / 4.0F;
                this.bipedLeftArm.rotateAngleX = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleX, ((float) Math.PI / 2F) - ((float) Math.PI / 2F) * f9);
                this.bipedRightArm.rotateAngleX = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleX, ((float) Math.PI / 2F) - ((float) Math.PI / 2F) * f9);
                this.bipedLeftArm.rotateAngleY = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleY, (float) Math.PI);
                this.bipedRightArm.rotateAngleY = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleY, (float) Math.PI);
                this.bipedLeftArm.rotateAngleZ = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleZ, (float) Math.PI);
                this.bipedRightArm.rotateAngleZ = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleZ, (float) Math.PI);
            }

            this.bipedLeftLeg.rotateAngleX = MathHelper.lerp(this.swimAnimation, this.bipedLeftLeg.rotateAngleX, 0.3F * MathHelper.cos(limbSwing * 0.33333334F + (float) Math.PI));
            this.bipedRightLeg.rotateAngleX = MathHelper.lerp(this.swimAnimation, this.bipedRightLeg.rotateAngleX, 0.3F * MathHelper.cos(limbSwing * 0.33333334F));
        }
    }

    @Override
    public void setLivingAnimations(@Nonnull EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {

        if (entitylivingbaseIn instanceof IPlayerSwimming) {

            this.swimAnimation = ((IPlayerSwimming) entitylivingbaseIn).getSwimAnimation(partialTickTime);
        }
    }

    private float getArmAngleSq(float limbSwing) {

        return -65.0F * limbSwing + limbSwing * limbSwing;
    }

    protected float rotLerpRad(float angleIn, float maxAngleIn, float mulIn) {

        float f = (mulIn - maxAngleIn) % ((float) Math.PI * 2F);
        if (f < -(float) Math.PI) {

            f += ((float) Math.PI * 2F);
        }

        if (f >= (float) Math.PI) {

            f -= ((float) Math.PI * 2F);
        }

        return maxAngleIn + angleIn * f;
    }

    @Shadow
    protected abstract EnumHandSide getMainHand(Entity entityIn);

    @Override
    public void setSwimAnimation(float swimAnimation) {

        this.swimAnimation = swimAnimation;
    }

}
