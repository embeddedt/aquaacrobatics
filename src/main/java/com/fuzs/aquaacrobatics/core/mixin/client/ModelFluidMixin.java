package com.fuzs.aquaacrobatics.core.mixin.client;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelFluid;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelFluid.class)
public class ModelFluidMixin {
    private ResourceLocation aqua$getRealStill(Fluid fluid) {
        if(fluid == FluidRegistry.WATER)
            return new ResourceLocation("aquaacrobatics:blocks/water_still");
        else
            return fluid.getStill();
    }

    private ResourceLocation aqua$getRealFlowing(Fluid fluid) {
        if(fluid == FluidRegistry.WATER)
            return new ResourceLocation("aquaacrobatics:blocks/water_flow");
        else
            return fluid.getFlowing();
    }
    
    @Redirect(method = "getTextures", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/Fluid;getStill()Lnet/minecraft/util/ResourceLocation;"), remap = false)
    private ResourceLocation getTextures_RealStill(Fluid fluid) {
        return aqua$getRealStill(fluid);
    }

    @Redirect(method = "getTextures", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/Fluid;getFlowing()Lnet/minecraft/util/ResourceLocation;"), remap = false)
    private ResourceLocation getTextures_RealFlowing(Fluid fluid) {
        return aqua$getRealFlowing(fluid);
    }

    @Redirect(method = "bake", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/Fluid;getStill()Lnet/minecraft/util/ResourceLocation;"), remap = false)
    private ResourceLocation bake_RealStill(Fluid fluid) {
        return aqua$getRealStill(fluid);
    }

    @Redirect(method = "bake", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/Fluid;getFlowing()Lnet/minecraft/util/ResourceLocation;"), remap = false)
    private ResourceLocation bake_RealFlowing(Fluid fluid) {
        return aqua$getRealFlowing(fluid);
    }
}
