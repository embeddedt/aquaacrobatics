package com.fuzs.aquaacrobatics.client.model;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import com.fuzs.aquaacrobatics.block.coral.BlockAbstractCoral;
import com.fuzs.aquaacrobatics.block.coral.BlockCoral;
import com.fuzs.aquaacrobatics.block.coral.BlockCoralFan;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class BlockCoralFanStateMapper extends StateMapperBase {
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        StringBuilder sb = new StringBuilder();
        if(state.getValue(BlockCoral.DEAD))
            sb.append("dead_");
        BlockAbstractCoral coral = (BlockAbstractCoral) state.getBlock();
        sb.append(coral.getCoralType(state).getName()).append("_coral");
        boolean isWall = state.getValue(BlockCoralFan.FACING) != EnumFacing.UP;
        if(isWall)
            sb.append("_wall");
        sb.append("_fan");
        String variant = isWall ? ("facing=" + state.getValue(BlockCoralFan.FACING).getName()) : "normal";
        return new ModelResourceLocation(new ResourceLocation(AquaAcrobatics.MODID, sb.toString()), variant);
    }
}
