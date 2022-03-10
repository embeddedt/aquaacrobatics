package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import com.fuzs.aquaacrobatics.block.*;
import com.fuzs.aquaacrobatics.biome.BiomeWaterFogColors;
import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.core.AquaAcrobaticsCore;
import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import com.fuzs.aquaacrobatics.integration.hats.HatsIntegration;
import com.fuzs.aquaacrobatics.item.DriedKelpItem;
import com.fuzs.aquaacrobatics.item.ExplorerMapItem;
import com.fuzs.aquaacrobatics.network.NetworkHandler;
import com.fuzs.aquaacrobatics.world.gen.WorldGenHandler;
import com.fuzs.aquaacrobatics.world.structure.BuriedTreasurePieces;
import com.fuzs.aquaacrobatics.world.structure.BuriedTreasureStructure;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
    @GameRegistry.ObjectHolder("aquaacrobatics:bubble_column")
    public static BlockBubbleColumn BUBBLE_COLUMN;

    public static Block blockDriedKelp;
    public static KelpTopBlock blockKelp;
    public static KelpBlock blockKelpPlant;
    public static SeagrassBlock blockSeagrass;

    public static ItemBlock itemDriedKelpBlock;
    public static ItemBlock itemKelp;
    public static ItemBlock itemSeagrass;
    public static Item itemDriedKelp;

    public static Item itemBuriedTreasureMap;

    private boolean needNetworking() {
        return ConfigHandler.MovementConfig.enableToggleCrawling;
    }

    public void onPreInit(FMLPreInitializationEvent event) {
        IntegrationManager.loadCompat();
        if(needNetworking())
            NetworkHandler.registerMessages(AquaAcrobatics.MODID);
        if(ConfigHandler.MiscellaneousConfig.aquaticWorldContent) {
            blockDriedKelp = new DriedKelpBlock();
            itemDriedKelpBlock = new DriedKelpItemBlock();
            AquaAcrobatics.REGISTRY.registerBlock(blockDriedKelp, itemDriedKelpBlock, "dried_kelp_block");

            blockKelpPlant = new KelpBlock();
            AquaAcrobatics.REGISTRY.getBlocks().add(blockKelpPlant);

            blockKelp = new KelpTopBlock();
            itemKelp = new ItemBlock(blockKelp);
            AquaAcrobatics.REGISTRY.registerBlock(blockKelp, itemKelp, "kelp");

            blockSeagrass = new SeagrassBlock();
            itemSeagrass = new ItemBlock(blockSeagrass);
            AquaAcrobatics.REGISTRY.registerBlock(blockSeagrass, itemSeagrass, "seagrass");

            itemDriedKelp = AquaAcrobatics.REGISTRY.registerItem(new DriedKelpItem(), "dried_kelp");

            itemBuriedTreasureMap = AquaAcrobatics.REGISTRY.registerItem(new ExplorerMapItem("Buried_Treasure"), "buried_treasure_map");
            MinecraftForge.EVENT_BUS.register(new WorldGenHandler());
        }
    }
    
    public void onInit() {
        if(ConfigHandler.MiscellaneousConfig.aquaticWorldContent) {
            GameRegistry.addSmelting(itemKelp, new ItemStack(itemDriedKelp), 0.1f);
            LootTableList.register(new ResourceLocation(AquaAcrobatics.MODID, "chests/buried_treasure"));
            MapGenStructureIO.registerStructure(BuriedTreasureStructure.Start.class, "Buried_Treasure");
            BuriedTreasurePieces.registerBuriedTreasurePieces();
        }
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
