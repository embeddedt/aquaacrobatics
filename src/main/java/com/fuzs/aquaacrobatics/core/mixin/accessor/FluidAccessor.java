package com.fuzs.aquaacrobatics.core.mixin.accessor;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Fluid.class)
public interface FluidAccessor {
    @Accessor(value = "still", remap = false)
    @Mutable
    void setStillTexture(ResourceLocation rl);
    @Accessor(value = "flowing", remap = false)
    @Mutable
    void setFlowingTexture(ResourceLocation rl);
}
