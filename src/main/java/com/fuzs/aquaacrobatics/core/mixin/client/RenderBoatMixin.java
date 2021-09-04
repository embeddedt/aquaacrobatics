package com.fuzs.aquaacrobatics.core.mixin.client;

import com.fuzs.aquaacrobatics.entity.IRockableBoat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderBoat.class)
public class RenderBoatMixin {
    @Inject(method = "setupRotation", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V", shift = At.Shift.BEFORE))
    private void addRockingRotation(EntityBoat boat, float entityYaw, float partialTicks, CallbackInfo ci) {
        float f2 = ((IRockableBoat)boat).getRockingAngle(partialTicks);
        if (!MathHelper.epsilonEquals(f2, 0.0F)) {
            GlStateManager.rotate(f2, 1.0F, 0.0F, 1.0F);
        }
    }
}
