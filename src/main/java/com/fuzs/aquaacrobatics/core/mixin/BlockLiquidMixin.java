package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockLiquid.class)
public abstract class BlockLiquidMixin extends Block {
    public BlockLiquidMixin(Material p_i45394_1_) {
        super(p_i45394_1_);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getLightOpacity(IBlockState state) {
        if(ConfigHandler.BlocksConfig.brighterWater && state.getMaterial() == Material.WATER)
            return 1;
        else
            return super.getLightOpacity(state);
    }
}
