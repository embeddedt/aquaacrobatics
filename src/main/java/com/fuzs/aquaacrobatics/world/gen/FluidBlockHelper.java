package com.fuzs.aquaacrobatics.world.gen;

import git.jbredwards.fluidlogged_api.common.util.FluidloggedUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class FluidBlockHelper {
    private static boolean isLiquidFluidlogged(World world, BlockPos position) {
        return !FluidloggedUtils.getFluidState(world, position).isEmpty();
    }
    public static boolean isLiquid(World world, BlockPos position) {
        if(Loader.isModLoaded("fluidlogged_api")) {
            return isLiquidFluidlogged(world, position);
        } else {
            return world.getBlockState(position).getMaterial().isLiquid();
        }
    }
}
