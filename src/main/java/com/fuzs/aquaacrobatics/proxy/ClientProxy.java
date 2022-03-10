package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.block.BlockBubbleColumn;
import com.fuzs.aquaacrobatics.client.handler.AirMeterHandler;
import com.fuzs.aquaacrobatics.client.handler.FogHandler;
import com.fuzs.aquaacrobatics.client.model.WaterResourcePack;
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
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {

        super.onPreInit(event);
        MinecraftForge.EVENT_BUS.register(new AirMeterHandler());
        MinecraftForge.EVENT_BUS.register(new FogHandler());

        if(ConfigHandler.BlocksConfig.newWaterColors) {
            List<IResourcePack> packs = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "field_110449_ao");
            packs.add(new WaterResourcePack(event.getSourceFile()));
            FMLClientHandler.instance().refreshResources(VanillaResourceType.TEXTURES);
        }
    }

    @Override
    public void onInit() {
        super.onInit();
        Keybindings.register();
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        if(ConfigHandler.MiscellaneousConfig.bubbleColumns)
            ModelLoader.setCustomStateMapper(CommonProxy.BUBBLE_COLUMN, new StateMap.Builder().ignore(BlockLiquid.LEVEL, BlockBubbleColumn.DRAG).build());
        if(ConfigHandler.MiscellaneousConfig.aquaticWorldContent) {
            CommonProxy.blockKelp.initModelOverride();
            CommonProxy.blockKelpPlant.initModelOverride();
            CommonProxy.blockSeagrass.initModelOverride();
        }
        
    }
    
    @SubscribeEvent
    public static void registerTextures(TextureStitchEvent.Pre event) {
        if(ConfigHandler.BlocksConfig.newWaterColors) {
            TextureMap map = event.getMap();
            /* Register the custom 1.13-style texture used by most in-world renderers */
            map.registerSprite(new ResourceLocation("aquaacrobatics:blocks/water_still"));
            map.registerSprite(new ResourceLocation("aquaacrobatics:blocks/water_flow"));
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
