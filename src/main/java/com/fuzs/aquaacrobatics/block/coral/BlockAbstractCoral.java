package com.fuzs.aquaacrobatics.block.coral;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Random;

public abstract class BlockAbstractCoral extends Block {
    public BlockAbstractCoral() {
        super(Material.ROCK);
    }

    public abstract IBlockState getDeadVersion(World worldIn, BlockPos pos, IBlockState current);

    public abstract boolean isDead(IBlockState state);

    public abstract BlockCoral.EnumType getCoralType(IBlockState state);

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.scheduleUpdate(pos, this, 60 + worldIn.rand.nextInt(40));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if(!isDead(state) && !isTouchingWater(worldIn, pos))
            worldIn.setBlockState(pos, getDeadVersion(worldIn, pos, state));
    }

    protected static boolean isTouchingWater(World worldIn, BlockPos pos) {
        if(FluidloggedUtils.getFluidState(worldIn, pos).getFluid() == FluidRegistry.WATER)
            return true;
        else {
            for(EnumFacing direction : EnumFacing.values()) {
                if(FluidloggedUtils.getFluidState(worldIn, pos.offset(direction)).getFluid() == FluidRegistry.WATER)
                    return true;
            }
        }
        return false;
    }
}
