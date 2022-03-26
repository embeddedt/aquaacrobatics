package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import com.fuzs.aquaacrobatics.biome.BiomeWaterFogColors;
import com.fuzs.aquaacrobatics.block.BlockBubbleColumn;
import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.core.AquaAcrobaticsCore;
import com.fuzs.aquaacrobatics.core.mixin.accessor.FluidAccessor;
import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import com.fuzs.aquaacrobatics.integration.hats.HatsIntegration;
import com.fuzs.aquaacrobatics.integration.witchery.WitcheryResurrectedIntegration;
import com.fuzs.aquaacrobatics.network.NetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
    @GameRegistry.ObjectHolder("aquaacrobatics:bubble_column")
    public static BlockBubbleColumn BUBBLE_COLUMN;

    private boolean needNetworking() {
        return ConfigHandler.MovementConfig.enableToggleCrawling;
    }

    public void onPreInit(FMLPreInitializationEvent event) {
        IntegrationManager.loadCompat();
        if(needNetworking())
            NetworkHandler.registerMessages(AquaAcrobatics.MODID);
    }

    public void onInit() {

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        if(ConfigHandler.MiscellaneousConfig.bubbleColumns)
            event.getRegistry().register(new BlockBubbleColumn());
    }


    public void onPostInit() {

        if (IntegrationManager.isHatsEnabled()) {

            HatsIntegration.register();
        }

        if (IntegrationManager.isWitcheryResurrectedEnabled())
            WitcheryResurrectedIntegration.register();

        if(!AquaAcrobaticsCore.isModCompatLoaded)
            AquaAcrobatics.LOGGER.error("Please consider installing MixinBooter to ensure compatibility with more mods");

        BiomeWaterFogColors.recomputeColors();
        // This code will print a warning if we don't have a color mapping for the biome
        /*
        for(Biome biome : Biome.REGISTRY) {
            biome.getWaterColor();
        }
         */
    }

}
