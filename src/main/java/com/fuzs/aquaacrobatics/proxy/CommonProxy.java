package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.block.BlockBubbleColumn;
import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import com.fuzs.aquaacrobatics.integration.hats.HatsIntegration;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
    @GameRegistry.ObjectHolder("aquaacrobatics:bubble_column")
    public static BlockBubbleColumn BUBBLE_COLUMN; 

    public void onPreInit() {

        IntegrationManager.loadCompat();
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
    }

}
