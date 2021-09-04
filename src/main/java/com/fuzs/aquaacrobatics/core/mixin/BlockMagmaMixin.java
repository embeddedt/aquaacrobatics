package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.block.BlockBubbleColumn;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMagma;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(BlockMagma.class)
public abstract class BlockMagmaMixin extends Block {
    public BlockMagmaMixin(Material p_i45394_1_) {
        super(p_i45394_1_);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(IBlockState state, World worldIn, BlockPos thisPos, Block blockIn, BlockPos fromPos) {
        if(fromPos.down().equals(thisPos) && worldIn.getBlockState(fromPos).getBlock() == Blocks.WATER) {
            worldIn.scheduleUpdate(thisPos, this, this.tickRate(worldIn));
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    @Override
    public int tickRate(World worldIn) {
        return 20;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        BlockBubbleColumn.placeBubbleColumn(worldIn, pos.up(), false);
    }

}
