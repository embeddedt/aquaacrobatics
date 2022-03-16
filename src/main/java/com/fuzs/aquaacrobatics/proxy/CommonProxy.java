package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import com.fuzs.aquaacrobatics.block.*;
import com.fuzs.aquaacrobatics.biome.BiomeWaterFogColors;
import com.fuzs.aquaacrobatics.block.coral.BlockCoral;
import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.core.AquaAcrobaticsCore;
import com.fuzs.aquaacrobatics.effect.PotionConduitPower;
import com.fuzs.aquaacrobatics.entity.EntityDrowned;
import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import com.fuzs.aquaacrobatics.integration.hats.HatsIntegration;
import com.fuzs.aquaacrobatics.item.DriedKelpItem;
import com.fuzs.aquaacrobatics.item.ExplorerMapItem;
import com.fuzs.aquaacrobatics.item.ItemBlockCoral;
import com.fuzs.aquaacrobatics.network.NetworkHandler;
import com.fuzs.aquaacrobatics.tile.TileEntityConduit;
import com.fuzs.aquaacrobatics.world.gen.WorldGenHandler;
import com.fuzs.aquaacrobatics.world.structure.BuriedTreasurePieces;
import com.fuzs.aquaacrobatics.world.structure.BuriedTreasureStructure;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
    @GameRegistry.ObjectHolder("aquaacrobatics:bubble_column")
    public static BlockBubbleColumn BUBBLE_COLUMN;

    public static Block blockDriedKelp;
    public static Block blockKelp;
    public static Block blockKelpPlant;
    public static Block blockConduit;
    public static Block blockCoralBlock;
    public static SeagrassBlock blockSeagrass;

    public static ItemBlock itemDriedKelpBlock;
    public static ItemBlock itemKelp;
    public static ItemBlock itemSeagrass;
    public static ItemBlock itemConduit;
    public static ItemBlock itemCoralBlock;
    public static Item itemDriedKelp;
    public static Item itemSeaHeart;
    public static Item itemNautilusShell;

    public static Item itemBuriedTreasureMap;

    @GameRegistry.ObjectHolder("aquaacrobatics:conduit_power")
    public static Potion effectConduitPower;

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

            blockConduit = new ConduitBlock();
            itemConduit = new ItemBlock(blockConduit);
            AquaAcrobatics.REGISTRY.registerBlock(blockConduit, itemConduit, "conduit");
            GameRegistry.registerTileEntity(TileEntityConduit.class, new ResourceLocation(AquaAcrobatics.MODID, "conduit"));

            blockCoralBlock = new BlockCoral();
            itemCoralBlock = new ItemBlockCoral(blockCoralBlock);
            AquaAcrobatics.REGISTRY.registerBlock(blockCoralBlock, itemCoralBlock, "coral_block");

            itemDriedKelp = AquaAcrobatics.REGISTRY.registerItem(new DriedKelpItem(), "dried_kelp");

            itemSeaHeart = AquaAcrobatics.REGISTRY.registerItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), "heart_of_the_sea");
            itemNautilusShell = AquaAcrobatics.REGISTRY.registerItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), "nautilus_shell");

            itemBuriedTreasureMap = AquaAcrobatics.REGISTRY.registerItem(new ExplorerMapItem("Buried_Treasure"), "buried_treasure_map");


            int entityNetworkId = 1;
            AquaAcrobatics.REGISTRY.registerMob(EntityDrowned.class, "drowned", entityNetworkId++, 9433559, 7969893);
            EntityRegistry.addSpawn(EntityDrowned.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.FROZEN_OCEAN, Biomes.RIVER, Biomes.FROZEN_RIVER);
            EntitySpawnPlacementRegistry.setPlacementType(EntityDrowned.class, EntityLiving.SpawnPlacementType.IN_WATER);

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


    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        if(ConfigHandler.MiscellaneousConfig.bubbleColumns)
            event.getRegistry().register(new PotionConduitPower());
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
