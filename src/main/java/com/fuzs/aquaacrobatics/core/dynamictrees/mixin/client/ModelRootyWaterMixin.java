package com.fuzs.aquaacrobatics.core.dynamictrees.mixin.client;

import com.ferreusveritas.dynamictrees.models.ModelRootyWater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ModelRootyWater.class)
public class ModelRootyWaterMixin {
    @ModifyConstant(method = "<init>", constant = @Constant(stringValue = "minecraft:blocks/water_still"))
    private String getStillTexture(String original) {
        return "aquaacrobatics:blocks/water_still";
    }
    @ModifyConstant(method = "<init>", constant = @Constant(stringValue = "minecraft:blocks/water_flow"))
    private String getFlowingTexture(String original) {
        return "aquaacrobatics:blocks/water_flow";
    }
}
