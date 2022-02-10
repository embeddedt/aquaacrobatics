package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.block.BlockBubbleColumn;
import com.fuzs.aquaacrobatics.client.handler.AirMeterHandler;
import com.fuzs.aquaacrobatics.client.handler.FogHandler;
import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import com.fuzs.aquaacrobatics.integration.artemislib.ArtemisLibIntegration;
import com.fuzs.aquaacrobatics.integration.enderio.EnderIOIntegration;
import com.fuzs.aquaacrobatics.integration.mobends.MoBendsIntegration;
import com.fuzs.aquaacrobatics.integration.thaumicaugmentation.ThaumicAugmentationIntegration;
import com.fuzs.aquaacrobatics.network.NetworkHandler;
import com.fuzs.aquaacrobatics.network.message.PacketSendKey;
import com.fuzs.aquaacrobatics.util.Keybindings;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
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

    @Override
    public void onInit() {
        Keybindings.register();
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        if(ConfigHandler.MiscellaneousConfig.bubbleColumns)
            ModelLoader.setCustomStateMapper(CommonProxy.BUBBLE_COLUMN, new StateMap.Builder().ignore(BlockLiquid.LEVEL, BlockBubbleColumn.DRAG).build());
    }
    
    @SubscribeEvent
    public static void registerTextures(TextureStitchEvent.Pre event) {
        if(ConfigHandler.BlocksConfig.newWaterColors) {
            TextureMap map = event.getMap();
            /* Register the custom 1.13-style texture used by most in-world renderers */
            map.registerSprite(new ResourceLocation("aquaacrobatics:blocks/water_still"));
            map.registerSprite(new ResourceLocation("aquaacrobatics:blocks/water_flow"));
            /* Register the compatibility sprites provided for mods which expect a blue texture */
            TextureAtlasSprite blueStill = map.registerSprite(new ResourceLocation("aquaacrobatics:blocks/water_still_blue"));
            TextureAtlasSprite blueFlow = map.registerSprite(new ResourceLocation("aquaacrobatics:blocks/water_flow_blue"));
            TextureAtlasSprite newOverlay = map.registerSprite(new ResourceLocation("aquaacrobatics:blocks/water_overlay"));
            map.mapRegisteredSprites.put("minecraft:blocks/water_still", blueStill);
            map.mapRegisteredSprites.put("minecraft:blocks/water_flow", blueFlow);
            map.mapRegisteredSprites.put("minecraft:blocks/water_overlay", newOverlay);
        }
    }

    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
        if(ConfigHandler.MovementConfig.enableToggleCrawling && Keybindings.forceCrawling.isPressed()) {
            IPlayerResizeable player = (IPlayerResizeable) Minecraft.getMinecraft().player;
            if(player != null) {
                if(player.canForceCrawling())
                    NetworkHandler.INSTANCE.sendToServer(new PacketSendKey(PacketSendKey.KeybindPacket.TOGGLE_CRAWLING));
                else {
                    ((EntityPlayerSP)player).sendMessage(new TextComponentTranslation("chat.aquaacrobatics.cannot_toggle_crawling"));
                }
            }
        }
    }

    @Override
    public void onPostInit() {

        super.onPostInit();
        FogHandler.recomputeBlacklist();
        if (IntegrationManager.isMoBendsEnabled()) {

            MoBendsIntegration.register();
        }

        if (IntegrationManager.isArtemisLibEnabled()) {

            ArtemisLibIntegration.register();
        }

        if(IntegrationManager.isEnderIoEnabled()) {
            EnderIOIntegration.register();
        }

        if(IntegrationManager.isThaumicAugmentationEnabled()) {
            ThaumicAugmentationIntegration.register();
        }
    }

}
