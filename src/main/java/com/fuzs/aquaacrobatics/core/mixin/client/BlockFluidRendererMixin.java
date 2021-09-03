package com.fuzs.aquaacrobatics.core.mixin.client;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BlockFluidRenderer.class)
public class BlockFluidRendererMixin {
    @Shadow @Final private BlockColors blockColors;

    @ModifyArgs(
            method = "renderFluid", at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/BufferBuilder;color(FFFF)Lnet/minecraft/client/renderer/BufferBuilder;"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;down()Lnet/minecraft/util/math/BlockPos;", ordinal = 0),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;add(III)Lnet/minecraft/util/math/BlockPos;", ordinal = 0)
            ),
            expect = 4,
            require = 4
    )
    private void overrideColorForBottomFace(Args args, IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder bufferBuilderIn) {
        if(blockStateIn.getMaterial() == Material.WATER && ((Float)args.get(1)) == 0.5f && ((Float)args.get(2)) == 0.5f) {
            int i = blockColors.colorMultiplier(blockStateIn, blockAccess, blockPosIn, 0);
            float f = (float)(i >> 16 & 255) / 255.0F;
            float f1 = (float)(i >> 8 & 255) / 255.0F;
            float f2 = (float)(i & 255) / 255.0F;
            args.set(0, f);
            args.set(1, f1);
            args.set(2, f2);
            args.set(3, 1.0f);   
        }
    }
}
