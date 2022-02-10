package com.fuzs.aquaacrobatics.core.xaerosminimap.mixin.client;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xaero.common.minimap.write.MinimapWriter;

@Mixin(MinimapWriter.class)
public class MinimapWriterMixin {
    @Redirect(method = "loadBlockColourFromTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockModelShapes;getTexture(Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
    private TextureAtlasSprite useWaterTexture(BlockModelShapes instance, IBlockState state) {
        Block block = state.getBlock();
        if(ConfigHandler.BlocksConfig.newWaterColors && (block == Blocks.WATER || block == Blocks.FLOWING_WATER))
            return FMLClientHandler.instance().getClient().getTextureMapBlocks().getAtlasSprite("aquaacrobatics:blocks/water_still");
        else
            return instance.getTexture(state);
    }
}
