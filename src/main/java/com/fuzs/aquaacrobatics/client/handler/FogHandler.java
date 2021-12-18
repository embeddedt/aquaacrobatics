package com.fuzs.aquaacrobatics.client.handler;

import com.fuzs.aquaacrobatics.biome.BiomeWaterFogColors;
import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import com.fuzs.aquaacrobatics.proxy.CommonProxy;
import com.fuzs.aquaacrobatics.util.math.MathHelperNew;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

/**
 * Uses Forge events to adjust water rendering so it more closely approximates 1.13+.
 *
 * Some of the code in this class is based off of Minecraft 1.16.
 */
public class FogHandler {

    private int targetFogColor = -1;
    private int prevFogColor = -1;
    private long fogAdjustTime = -1L;

    @SubscribeEvent
    public void registerBlockColors(ColorHandlerEvent.Block event){
        if(ConfigHandler.MiscellaneousConfig.bubbleColumns)
            event.getBlockColors().registerBlockColorHandler(new IBlockColor()
            {
                public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
                {
                    return worldIn != null && pos != null ? BiomeColorHelper.getWaterColorAtPos(worldIn, pos) : -1;
                }
    
            }, CommonProxy.BUBBLE_COLUMN);
    }
    
    @SubscribeEvent
    public void onRenderFogDensity(EntityViewRenderEvent.FogDensity event) {
        if(event.getState().getMaterial() == Material.WATER) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP2);
            float density = 0.05f;
            if(event.getEntity() instanceof EntityPlayer) {
                EntityPlayer playerEntity = (EntityPlayer)event.getEntity();
                float waterVision = ((IPlayerResizeable)playerEntity).getWaterVision();
                density -= waterVision * waterVision * 0.03F;
                Biome biome = playerEntity.world.getBiome(playerEntity.getPosition());
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
                    density += 0.005F;
                }
            }
            event.setDensity(density);
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onGetWaterColor(BiomeEvent.GetWaterColor event) {
        BiomeWaterFogColors.getWaterColorForBiome(event);
    }

    /* LOW to override mods like Biomes O' Plenty which force their own underwater fog color */
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderFogColor(EntityViewRenderEvent.FogColors event) {
        Block blockInside = event.getState().getBlock();
        if((event.getState().getMaterial() == Material.WATER) && event.getEntity() instanceof EntityPlayer) {
            float fogRed, fogGreen, fogBlue;
            EntityPlayer playerEntity = (EntityPlayer) event.getEntity();
            long i = System.nanoTime() / 1000000L;
            int j = BiomeWaterFogColors.getWaterFogColorForBiome(playerEntity.world.getBiome(playerEntity.getPosition()));
            if (fogAdjustTime < 0L) {
                targetFogColor = j;
                prevFogColor = j;
                fogAdjustTime = i;
            }
            int k = targetFogColor >> 16 & 255;
            int l = targetFogColor >> 8 & 255;
            int i1 = targetFogColor & 255;
            int j1 = prevFogColor >> 16 & 255;
            int k1 = prevFogColor >> 8 & 255;
            int l1 = prevFogColor & 255;
            float f = MathHelper.clamp((float)(i - fogAdjustTime) / 5000.0F, 0.0F, 1.0F);
            float f1 = MathHelperNew.lerp(f, (float)j1, (float)k);
            float f2 = MathHelperNew.lerp(f, (float)k1, (float)l);
            float f3 = MathHelperNew.lerp(f, (float)l1, (float)i1);
            fogRed = f1 / 255.0F;
            fogGreen = f2 / 255.0F;
            fogBlue = f3 / 255.0F;
            if (targetFogColor != j) {
                targetFogColor = j;
                prevFogColor = MathHelper.floor(f1) << 16 | MathHelper.floor(f2) << 8 | MathHelper.floor(f3);
                fogAdjustTime = i;
            }
            float f6 = ((IPlayerResizeable)playerEntity).getWaterVision();
            float f9 = Math.min(1.0F / fogRed, Math.min(1.0F / fogGreen, 1.0F / fogBlue));
            fogRed = fogRed * (1.0F - f6) + fogRed * f9 * f6;
            fogGreen = fogGreen * (1.0F - f6) + fogGreen * f9 * f6;
            fogBlue = fogBlue * (1.0F - f6) + fogBlue * f9 * f6;

            event.setRed(fogRed);
            event.setGreen(fogGreen);
            event.setBlue(fogBlue);
        } else if((blockInside == Blocks.LAVA || blockInside == Blocks.FLOWING_LAVA)) {
            event.setRed(0.6f);
            event.setGreen(0.1f);
            event.setBlue(0.0f);
            fogAdjustTime = -1L;
        } else {
            fogAdjustTime = -1L;
        }
    }
}
