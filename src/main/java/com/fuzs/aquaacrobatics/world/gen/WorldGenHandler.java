package com.fuzs.aquaacrobatics.world.gen;

import com.fuzs.aquaacrobatics.block.KelpTopBlock;
import com.fuzs.aquaacrobatics.block.SeagrassBlock;
import com.fuzs.aquaacrobatics.proxy.CommonProxy;
import com.fuzs.aquaacrobatics.world.structure.BuriedTreasureStructure;
import com.fuzs.aquaacrobatics.world.structure.ShipwreckStructure;
import git.jbredwards.fluidlogged_api.api.block.IFluidloggable;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class WorldGenHandler {
    private static final BuriedTreasureStructure buriedTreasure = new BuriedTreasureStructure();
    private static final ShipwreckStructure shipwreck = new ShipwreckStructure();

    @SubscribeEvent
    public void onDecorateOverworld(PopulateChunkEvent.Post event) {
        if(event.getGenerator() instanceof ChunkGeneratorOverworld) {
            buriedTreasure.generateStructure(event.getWorld(), event.getRand(), new ChunkPos(event.getChunkX(), event.getChunkZ()));
            shipwreck.generateStructure(event.getWorld(), event.getRand(), new ChunkPos(event.getChunkX(), event.getChunkZ()));
        }
    }

    public static void onChunkGenerate(World world, int x, int z, ChunkPrimer chunkPrimer) {
        buriedTreasure.generate(world, x, z, chunkPrimer);
        shipwreck.generate(world, x, z, chunkPrimer);
    }

    @SubscribeEvent
    public void onDecorateKelp(PopulateChunkEvent.Post e) {
        Random rand = e.getRand();
        World world = e.getWorld();
        /* Kelp */
        for (int i = 0; i < 40; i++) {
            int seaLevel = Math.max(world.getSeaLevel() - 1, 1);
            BlockPos pos = new BlockPos(rand.nextInt(16) + e.getChunkX() * 16 + 8, seaLevel, rand.nextInt(16) + e.getChunkZ() * 16 + 8);
            Biome biome = world.getBiomeProvider().getBiome(pos);

            if (this.canGenerateKelpInBiome(biome)) {
                pos = getSeaBed(world, pos);
                this.attemptGenerateKelp(world, rand, pos);
            }
        }

        for (int i = 0; i < 48; i++) {
            int seaLevel = Math.max(world.getSeaLevel() - 1, 1);
            BlockPos pos = new BlockPos(rand.nextInt(16) + e.getChunkX() * 16 + 8, seaLevel, rand.nextInt(16) + e.getChunkZ() * 16 + 8);
            Biome biome = world.getBiomeProvider().getBiome(pos);

            if (this.canGenerateSeagrassInBiome(biome)) {
                pos = getSeaBed(world, pos);
                this.attemptGenerateSeagrass(world, rand, pos);
            }
        }
    }

    private boolean isTrueWater(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        return block == Blocks.WATER || block == Blocks.FLOWING_WATER;
    }

    private void attemptGenerateKelp(World world, Random rand, BlockPos startPosition) {
        Block below = world.getBlockState(startPosition.down()).getBlock();
        if(below instanceof IFluidloggable) {
            /* TODO revisit this and find a better way of not putting plants on top of plants */
            return;
        }
        int blocksToGenerate = rand.nextInt(10) + 1;
        IBlockState topState = CommonProxy.blockKelp.getDefaultState().withProperty(KelpTopBlock.AGE, 15);
        BlockPos.MutableBlockPos currentPosition = new BlockPos.MutableBlockPos(startPosition);
        boolean placedOneKelp = false;
        for(int i = 0; i < blocksToGenerate; i++) {
            if(!isTrueWater(world, currentPosition) || world.isAirBlock(currentPosition.up()))
                break;
            world.setBlockState(currentPosition, CommonProxy.blockKelpPlant.getDefaultState());
            currentPosition.setY(currentPosition.getY() + 1);
            placedOneKelp = true;
        }

        if(placedOneKelp) {
            currentPosition.setY(currentPosition.getY() - 1);
            world.setBlockState(currentPosition, topState);
        }
    }

    private void attemptGenerateSeagrass(World world, Random rand, BlockPos pos) {
        Block below = world.getBlockState(pos.down()).getBlock();
        if(below instanceof IFluidloggable) {
            /* TODO revisit this and find a better way of not putting plants on top of plants */
            return;
        }
        if(!isTrueWater(world, pos))
            return;
        boolean shouldBeTall = isTrueWater(world, pos.up()) && rand.nextBoolean();
        world.setBlockState(pos, CommonProxy.blockSeagrass.getDefaultState().withProperty(SeagrassBlock.TYPE, shouldBeTall ?
                SeagrassBlock.SeagrassType.LOWER : SeagrassBlock.SeagrassType.SINGLE));
        if(shouldBeTall) {
            world.setBlockState(pos.up(), CommonProxy.blockSeagrass.getDefaultState().withProperty(SeagrassBlock.TYPE, SeagrassBlock.SeagrassType.UPPER));
        }
    }

    public static BlockPos getSeaBed(World world, BlockPos pos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
        while (!FluidloggedUtils.getFluidState(world, mutablePos).isEmpty()) {
            mutablePos.setY(mutablePos.getY() - 1);
        }
        mutablePos.setY(mutablePos.getY() + 1);
        return mutablePos.toImmutable();
    }

    private boolean canGenerateKelpInBiome(Biome biome) {
        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN);
    }

    private boolean canGenerateSeagrassInBiome(Biome biome) {
        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER);
    }
}
