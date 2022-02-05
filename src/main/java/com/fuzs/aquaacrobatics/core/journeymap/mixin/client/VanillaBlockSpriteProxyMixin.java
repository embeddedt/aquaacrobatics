package com.fuzs.aquaacrobatics.core.journeymap.mixin.client;

import journeymap.client.cartography.color.ColoredSprite;
import journeymap.client.mod.vanilla.VanillaBlockSpriteProxy;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Collections;

@Mixin(VanillaBlockSpriteProxy.class)
public class VanillaBlockSpriteProxyMixin {
    @Inject(
            method = "getSprites(Ljourneymap/client/model/BlockMD;Ljourneymap/client/model/ChunkMD;Lnet/minecraft/util/math/BlockPos;)Ljava/util/Collection;",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void getSprites(BlockMD blockMD, ChunkMD facing, BlockPos state, CallbackInfoReturnable<Collection<ColoredSprite>> cir) {
        Block block = blockMD.getBlockState().getBlock();
        if(block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
            TextureAtlasSprite tas = FMLClientHandler.instance().getClient().getTextureMapBlocks().getAtlasSprite("aquaacrobatics:blocks/water_still");
            cir.setReturnValue(Collections.singletonList(new ColoredSprite(tas, null)));
        }
    }
}
