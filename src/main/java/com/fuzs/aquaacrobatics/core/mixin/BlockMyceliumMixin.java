package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.core.UnderwaterGrassLikeHandler;
import net.minecraft.block.BlockMycelium;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

/* MC-130137 */
@Mixin(BlockMycelium.class)
public abstract class BlockMyceliumMixin {
    @Inject(method = "updateTick", at = @At("HEAD"), cancellable = true)
    private void updateUnderwaterToDirt(World world, BlockPos pos, IBlockState state, Random rand, CallbackInfo ci) {
        UnderwaterGrassLikeHandler.handleUnderwaterGrassLikeBlock(world, pos, state, rand, ci);
    }

    @Redirect(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)Z", ordinal = 1), require = 0)
    public boolean avoidSettingGrass(World world, BlockPos pos, IBlockState state) {
        if(world.getBlockState(pos.up()).getMaterial().isLiquid())
            return false;
        return world.setBlockState(pos, state);
    }
}
