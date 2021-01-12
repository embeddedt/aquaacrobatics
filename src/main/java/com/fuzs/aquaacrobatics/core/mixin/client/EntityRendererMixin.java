package com.fuzs.aquaacrobatics.core.mixin.client;

import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import com.fuzs.aquaacrobatics.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @Shadow
    @Final
    private Minecraft mc;

    private float eyeHeight;
    private float previousEyeHeight;
    private float entityEyeHeight;

    @ModifyVariable(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;prevPosX:D", ordinal = 0), ordinal = 1)
    public float getEyeHeight(float eyeHeight, float partialTicks) {

        // random patches has this feature as well
        if (IntegrationManager.isRandomPatchesEnabled()) {

            return eyeHeight;
        }

        // need to do it like this to prevent crash with wings mod
        this.entityEyeHeight = eyeHeight;
        return MathHelper.lerp(partialTicks, this.previousEyeHeight, this.eyeHeight);
    }

    @Inject(method = "updateRenderer", at = @At("TAIL"))
    public void updateRenderer(CallbackInfo callbackInfo) {

        this.interpolateHeight();
    }

    private void interpolateHeight() {

        this.previousEyeHeight = this.eyeHeight;
        this.eyeHeight += (this.entityEyeHeight - this.eyeHeight) * 0.5F;
    }

}
