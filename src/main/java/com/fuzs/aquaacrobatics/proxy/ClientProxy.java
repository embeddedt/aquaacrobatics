package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.block.BlockBubbleColumn;
import com.fuzs.aquaacrobatics.client.handler.AirMeterHandler;
import com.fuzs.aquaacrobatics.client.handler.FogHandler;
import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import com.fuzs.aquaacrobatics.integration.artemislib.ArtemisLibIntegration;
import com.fuzs.aquaacrobatics.integration.enderio.EnderIOIntegration;
import com.fuzs.aquaacrobatics.integration.mobends.MoBendsIntegration;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void onPreInit() {

        super.onPreInit();
        MinecraftForge.EVENT_BUS.register(new AirMeterHandler());
        MinecraftForge.EVENT_BUS.register(new FogHandler());
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        if(ConfigHandler.MiscellaneousConfig.bubbleColumns)
            ModelLoader.setCustomStateMapper(CommonProxy.BUBBLE_COLUMN, new StateMap.Builder().ignore(BlockLiquid.LEVEL, BlockBubbleColumn.DRAG).build());
    }

    @Override
    public void onPostInit() {

        super.onPostInit();
        if (IntegrationManager.isMoBendsEnabled()) {

            MoBendsIntegration.register();
        }

        if (IntegrationManager.isArtemisLibEnabled()) {

            ArtemisLibIntegration.register();
        }

        if(IntegrationManager.isEnderIoEnabled()) {
            EnderIOIntegration.register();
        }
    }

}
