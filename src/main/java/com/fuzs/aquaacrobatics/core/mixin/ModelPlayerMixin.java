package com.fuzs.aquaacrobatics.core.mixin;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
@Mixin(ModelPlayer.class)
public abstract class ModelPlayerMixin extends ModelBiped {

    @Shadow
    @Final
    private ModelRenderer bipedCape;

    @Inject(method = "setRotationAngles", at = @At("TAIL"))
    public void setRotationAnglesPost(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, @Nonnull Entity entityIn, CallbackInfo callbackInfo) {

        // fixes MC-52178 by resetting rotation points all over again
        if (((EntityLivingBase) entityIn).getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()) {

            if (entityIn.isSneaking()) {

                this.bipedCape.rotationPointZ = 1.4F;
                this.bipedCape.rotationPointY = 1.85F;
            } else {

                this.bipedCape.rotationPointZ = 0.0F;
                this.bipedCape.rotationPointY = 0.0F;
            }
        } else if (entityIn.isSneaking()) {

            this.bipedCape.rotationPointZ = 0.3F;
            this.bipedCape.rotationPointY = 0.8F;
        } else {

            this.bipedCape.rotationPointZ = -1.1F;
            this.bipedCape.rotationPointY = -0.85F;
        }
    }

}
