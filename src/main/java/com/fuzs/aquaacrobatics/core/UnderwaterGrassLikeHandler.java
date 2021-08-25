package com.fuzs.aquaacrobatics.core;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

public class UnderwaterGrassLikeHandler {
    public static void handleUnderwaterGrassLikeBlock(World world, BlockPos pos, IBlockState state, Random rand, CallbackInfo ci) {
        if(world.isRemote || !world.isAreaLoaded(pos, 3)) {
            ci.cancel();
            return;
        }
        IBlockState above = world.getBlockState(pos.up());
        if(above.getMaterial().isLiquid()) {
            world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            ci.cancel();
        }
    }
}
