package com.fuzs.aquaacrobatics.core.mixin.client;

import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import com.fuzs.aquaacrobatics.integration.mobends.MoBendsIntegration;
import com.fuzs.aquaacrobatics.entity.player.IModelBipedSwimming;
import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import com.fuzs.aquaacrobatics.util.math.MathHelper;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(RenderPlayer.class)
public abstract class RenderPlayerMixin extends RenderLivingBase<AbstractClientPlayer> {

    public RenderPlayerMixin(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {

        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    @Inject(method = "renderRightArm", at = @At(value = "FIELD", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/model/ModelPlayer;isSneak:Z"))
    public void renderRightArm(AbstractClientPlayer clientPlayer, CallbackInfo callbackInfo) {

        ModelPlayer modelplayer = (ModelPlayer) this.getMainModel();
        ((IModelBipedSwimming) modelplayer).setSwimAnimation(0.0F);
    }

    @Inject(method = "renderLeftArm", at = @At(value = "FIELD", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/model/ModelPlayer;isSneak:Z"))
    public void renderLeftArm(AbstractClientPlayer clientPlayer, CallbackInfo callbackInfo) {

        ModelPlayer modelplayer = (ModelPlayer) this.getMainModel();
        ((IModelBipedSwimming) modelplayer).setSwimAnimation(0.0F);
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "applyRotations", at = @At("TAIL"))
    protected void applyRotations(AbstractClientPlayer entityLiving, float p_77043_2_, float rotationYaw, float partialTicks, CallbackInfo callbackInfo) {

        if (!entityLiving.isElytraFlying()) {

            if (!IntegrationManager.isMoBendsEnabled()) {

                float f = ((IPlayerResizeable) entityLiving).getSwimAnimation(partialTicks);
                float f3 = entityLiving.isInWater() ? -90.0F - entityLiving.rotationPitch : -90.0F;
                float f4 = MathHelper.lerp(f, 0.0F, f3);
                GlStateManager.rotate(f4, 1.0F, 0.0F, 0.0F);
            }

            if (((IPlayerResizeable) entityLiving).isActuallySwimming()) {

                if (!IntegrationManager.isMoBendsEnabled()) {

                    GlStateManager.translate(0.0F, -1.0F, 0.3F);
                } else {

                    MoBendsIntegration.applyRotations((RenderPlayer) (Object) this, entityLiving);
                }
            }
        }
    }

}
