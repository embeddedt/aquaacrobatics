package com.fuzs.aquaacrobatics.block.coral;

import git.jbredwards.fluidlogged_api.api.block.IFluidloggable;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nonnull;
import java.util.Random;

@Optional.Interface(iface = "git.jbredwards.fluidlogged_api.api.block.IFluidloggable", modid = "fluidlogged_api")
public abstract class BlockAbstractCoral extends Block implements IFluidloggable {
    public BlockAbstractCoral() {
        super(Material.ROCK);
    }

    @Override
    public boolean isFluidValid(@Nonnull IBlockState state, @Nonnull Fluid fluid) {
        return fluid == FluidRegistry.WATER;
    }

    @Override
    public boolean canFluidFlow(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState here, @Nonnull EnumFacing side) {
        return true;
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

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return 0;
    }
}
