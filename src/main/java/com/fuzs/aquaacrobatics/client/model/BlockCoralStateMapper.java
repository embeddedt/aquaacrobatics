package com.fuzs.aquaacrobatics.client.model;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

public class BlockCoralStateMapper extends StateMapperBase {
    private final PropertyEnum<?> variantProperty;
    private final PropertyBool deadProperty;
    private final String suffix;

    public BlockCoralStateMapper(PropertyEnum<?> variantProperty, PropertyBool deadProperty, String suffix) {
        this.variantProperty = variantProperty;
        this.deadProperty = deadProperty;
        this.suffix = suffix;
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        String variant = state.getValue(variantProperty).getName();
        StringBuilder sb = new StringBuilder();
        if(state.getValue(deadProperty))
            sb.append("dead_");
        sb.append(variant).append("_");
        sb.append(this.suffix);
        return new ModelResourceLocation(new ResourceLocation(AquaAcrobatics.MODID, sb.toString()), "normal");
    }
}
