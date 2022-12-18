package com.fuzs.aquaacrobatics.core.thaumcraft.mixin.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thaumcraft.client.renderers.tile.TileCrucibleRenderer;

@Mixin(TileCrucibleRenderer.class)
public class TileCrucibleRendererMixin {
    @Redirect(method = "renderFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockModelShapes;getTexture(Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
    private TextureAtlasSprite getLegacyWaterTexture(BlockModelShapes instance, IBlockState state) {
        if(state == Blocks.WATER.getDefaultState())
            return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still");
        return instance.getTexture(state);
    }
}
