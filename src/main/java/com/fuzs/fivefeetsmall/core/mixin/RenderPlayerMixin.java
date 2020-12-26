package com.fuzs.fivefeetsmall.core.mixin;

import com.fuzs.fivefeetsmall.entity.player.IModelPlayerSwimming;
import com.fuzs.fivefeetsmall.entity.player.IPlayerSPSwimming;
import com.fuzs.fivefeetsmall.entity.player.IPlayerSwimming;
import com.fuzs.fivefeetsmall.util.MathHelper;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class RenderPlayerMixin extends RenderLivingBase<AbstractClientPlayer> {

    public RenderPlayerMixin(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {

        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    @Redirect(method = "doRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;isSneaking()Z"))
    public boolean shouldRenderSneakingDoRender(AbstractClientPlayer entity) {

        return ((IPlayerSPSwimming) entity).shouldRenderSneaking();
    }

    @Redirect(method = "setModelVisibilities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;isSneaking()Z"))
    public boolean shouldRenderSneakingSetModelVisibilities(AbstractClientPlayer clientPlayer) {

        return ((IPlayerSPSwimming) clientPlayer).shouldRenderSneaking();
    }

    @Inject(method = "renderRightArm", at = @At(value = "FIELD", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/model/ModelPlayer;isSneak:Z"))
    public void renderRightArm(AbstractClientPlayer clientPlayer, CallbackInfo callbackInfo) {

        ModelPlayer modelplayer = (ModelPlayer) this.getMainModel();
        ((IModelPlayerSwimming) modelplayer).setSwimAnimation(0.0F);
    }

    @Inject(method = "renderLeftArm", at = @At(value = "FIELD", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/model/ModelPlayer;isSneak:Z"))
    public void renderLeftArm(AbstractClientPlayer clientPlayer, CallbackInfo callbackInfo) {

        ModelPlayer modelplayer = (ModelPlayer) this.getMainModel();
        ((IModelPlayerSwimming) modelplayer).setSwimAnimation(0.0F);
    }

    @Inject(method = "applyRotations", at = @At("TAIL"))
    protected void applyRotations(AbstractClientPlayer entityLiving, float p_77043_2_, float rotationYaw, float partialTicks, CallbackInfo callbackInfo) {

        float f = ((IPlayerSwimming) entityLiving).getSwimAnimation(partialTicks);
        if (!entityLiving.isElytraFlying()) {

            float f3 = entityLiving.isInWater() ? -90.0F - entityLiving.rotationPitch : -90.0F;
            float f4 = MathHelper.lerp(f, 0.0F, f3);
            GlStateManager.rotate(f4, 1.0F, 0.0F, 0.0F);
            if (((IPlayerSwimming) entityLiving).isActuallySwimming()) {

                GlStateManager.translate(0.0F, -1.0F, 0.3F);
            }
        }
    }

}
