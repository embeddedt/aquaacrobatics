package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import com.fuzs.aquaacrobatics.block.BlockBubbleColumn;
import com.fuzs.aquaacrobatics.block.KelpBlock;
import com.fuzs.aquaacrobatics.block.KelpTopBlock;
import com.fuzs.aquaacrobatics.block.coral.BlockAbstractCoral;
import com.fuzs.aquaacrobatics.block.coral.BlockCoral;
import com.fuzs.aquaacrobatics.block.coral.BlockCoralFan;
import com.fuzs.aquaacrobatics.client.handler.AirMeterHandler;
import com.fuzs.aquaacrobatics.client.handler.FogHandler;
import com.fuzs.aquaacrobatics.client.model.BlockCoralFanStateMapper;
import com.fuzs.aquaacrobatics.client.model.BlockCoralStateMapper;
import com.fuzs.aquaacrobatics.client.model.WaterResourcePack;
import com.fuzs.aquaacrobatics.client.render.RenderDrowned;
import com.fuzs.aquaacrobatics.client.render.TileEntityConduitItemRenderer;
import com.fuzs.aquaacrobatics.client.render.TileEntityConduitRenderer;
import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.entity.EntityDrowned;
import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import com.fuzs.aquaacrobatics.integration.artemislib.ArtemisLibIntegration;
import com.fuzs.aquaacrobatics.integration.enderio.EnderIOIntegration;
import com.fuzs.aquaacrobatics.integration.mobends.MoBendsIntegration;
import com.fuzs.aquaacrobatics.integration.thaumicaugmentation.ThaumicAugmentationIntegration;
import com.fuzs.aquaacrobatics.item.ItemBlockCoral;
import com.fuzs.aquaacrobatics.network.NetworkHandler;
import com.fuzs.aquaacrobatics.network.message.PacketSendKey;
import com.fuzs.aquaacrobatics.tile.TileEntityConduit;
import com.fuzs.aquaacrobatics.util.Keybindings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityConduit.class, new TileEntityConduitRenderer());
        CommonProxy.itemConduit.setTileEntityItemStackRenderer(new TileEntityConduitItemRenderer());
    }

    private static void doItemAndBlockModel(Block block, Item item, StateMapperBase mapper) {
        ModelLoader.setCustomStateMapper(block, mapper);
        for(Map.Entry<IBlockState, ModelResourceLocation> e : mapper.putStateModelLocations(block).entrySet()) {
            ModelLoader.setCustomModelResourceLocation(item, block.getMetaFromState(e.getKey()), e.getValue());
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        if(ConfigHandler.MiscellaneousConfig.bubbleColumns)
            ModelLoader.setCustomStateMapper(CommonProxy.BUBBLE_COLUMN, new StateMap.Builder().ignore(BlockLiquid.LEVEL, BlockBubbleColumn.DRAG).build());
        if(ConfigHandler.MiscellaneousConfig.aquaticWorldContent) {
            ((KelpTopBlock)CommonProxy.blockKelp).initModelOverride();
            ((KelpBlock)CommonProxy.blockKelpPlant).initModelOverride();
            CommonProxy.blockSeagrass.initModelOverride();
            RenderingRegistry.registerEntityRenderingHandler(EntityDrowned.class, RenderDrowned.FACTORY);
            ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(AquaAcrobatics.MODID + ":conduit", "inventory");
            ModelLoader.setCustomModelResourceLocation(itemConduit, 0, itemModelResourceLocation);
            /* coral */
            doItemAndBlockModel(CommonProxy.blockCoralBlock, CommonProxy.itemCoralBlock, new BlockCoralStateMapper(BlockCoral.VARIANT, BlockCoral.DEAD, "coral_block"));
            doItemAndBlockModel(CommonProxy.blockCoralPlant, CommonProxy.itemCoralPlant, new BlockCoralStateMapper(BlockCoral.VARIANT, BlockCoral.DEAD, "coral"));
            for(BlockCoral.EnumType type : BlockCoral.EnumType.values()) {
                String base = type.getName() + "_coral_fan";
                ResourceLocation location = new ResourceLocation(AquaAcrobatics.MODID, base);
                Item item = Item.REGISTRY.getObject(location);
                BlockAbstractCoral block = (BlockAbstractCoral) Block.REGISTRY.getObject(location);
                ModelLoader.setCustomStateMapper(block, new BlockCoralFanStateMapper());
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(location, "inventory"));
                ModelLoader.setCustomModelResourceLocation(item, 8, new ModelResourceLocation(new ResourceLocation(AquaAcrobatics.MODID, "dead_" + base), "inventory"));
            }
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
