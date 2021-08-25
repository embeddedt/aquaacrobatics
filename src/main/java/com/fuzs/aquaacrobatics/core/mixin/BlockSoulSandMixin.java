package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.block.BlockBubbleColumn;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockSoulSand.class)
public abstract class BlockSoulSandMixin extends Block {
    public BlockSoulSandMixin(Material p_i45394_1_) {
        super(p_i45394_1_);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        BlockBubbleColumn.placeBubbleColumn(worldIn, pos.up(), true);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        BlockBubbleColumn.placeBubbleColumn(worldIn, pos.up(), true);
    }

}
