package com.fuzs.aquaacrobatics.core.mixin.client;

import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import com.fuzs.aquaacrobatics.util.math.MathHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
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
    private float partialTicks;

    @Inject(method = "orientCamera", at = @At("HEAD"))
    private void orientCamera(float partialTicks, CallbackInfo callbackInfo) {

        // field for passing on partialTicks, workaround as @ModifyVariable is unable to handle method arguments in Mixin <0.8
        this.partialTicks = partialTicks;
    }

    @ModifyVariable(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;prevPosX:D", ordinal = 0), ordinal = 1)
    public float getEyeHeight(float eyeHeight) {

        // random patches has this feature as well
        if (IntegrationManager.isRandomPatchesEnabled()) {

            return eyeHeight;
        }

        // need to do it like this to prevent crash with wings mod
        this.entityEyeHeight = eyeHeight;
        return MathHelper.lerp(this.partialTicks, this.previousEyeHeight, this.eyeHeight);
    }

    @Inject(method = "updateRenderer", at = @At("TAIL"))
    public void updateRenderer(CallbackInfo callbackInfo) {

        this.interpolateHeight();
    }

    private void interpolateHeight() {

        this.previousEyeHeight = this.eyeHeight;
        this.eyeHeight += (this.entityEyeHeight - this.eyeHeight) * 0.5F;
    }

    /**
     * This mixin is marked as not required, as some mods patch this themselves.
     */
    @Redirect(
            method = "renderWorldPass",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;isInsideOfMaterial(Lnet/minecraft/block/material/Material;)Z",
                    ordinal = 0
            ),
            require = 0,
            expect = 0
    )
    private boolean ignoreWater(Entity entity, Material material) {
        /* 1.13 removed this check */
        if(material == Material.WATER)
            return false;
        return entity.isInsideOfMaterial(material);
    }
}
