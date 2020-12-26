package com.fuzs.fivefeetsmall.core.mixin;

import com.fuzs.fivefeetsmall.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @Shadow
    @Final
    private Minecraft mc;

    private float height;
    private float previousHeight;

    @Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getEyeHeight()F"))
    public float getEyeHeight(Entity entity, float partialTicks) {

        return MathHelper.lerp(partialTicks, this.previousHeight, this.height);
    }

    @Inject(method = "updateRenderer", at = @At("TAIL"))
    public void updateRenderer(CallbackInfo callbackInfo) {

        this.interpolateHeight();
    }

    private void interpolateHeight() {

        Entity renderViewEntity = this.mc.getRenderViewEntity();
        if (renderViewEntity != null) {

            this.previousHeight = this.height;
            this.height += (renderViewEntity.getEyeHeight() - this.height) * 0.5F;
        }
    }

}
