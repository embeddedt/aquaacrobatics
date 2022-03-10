package com.fuzs.aquaacrobatics.world.gen;

import com.fuzs.aquaacrobatics.world.structure.BuriedTreasureStructure;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldGenHandler {
    private static final BuriedTreasureStructure buriedTreasure = new BuriedTreasureStructure();

    @SubscribeEvent
    public void onDecorateOverworld(PopulateChunkEvent.Post event) {
        if(event.getGenerator() instanceof ChunkGeneratorOverworld) {
            buriedTreasure.generateStructure(event.getWorld(), event.getRand(), new ChunkPos(event.getChunkX(), event.getChunkZ()));
        }
    }

    public static void onChunkGenerate(World world, int x, int z, ChunkPrimer chunkPrimer) {
        buriedTreasure.generate(world, x, z, chunkPrimer);
    }
}
