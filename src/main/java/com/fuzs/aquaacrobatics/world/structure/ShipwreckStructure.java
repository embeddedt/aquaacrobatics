package com.fuzs.aquaacrobatics.world.structure;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;

/**
 * Based on MCP 1.13.
 */
public class ShipwreckStructure extends MapGenStructure {
    protected final int maxDistanceBetweenScatteredFeatures = 16;
    public ShipwreckStructure() {
    }

    @Override
    public String getStructureName()
    {
        return "Shipwreck";
    }

    protected int getSeedZ() {
        return 165745295;
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        Random random = this.world.setRandomSeed(chunkX, chunkZ, getSeedZ());
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= this.maxDistanceBetweenScatteredFeatures - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= this.maxDistanceBetweenScatteredFeatures - 1;
        }

        int k = chunkX / this.maxDistanceBetweenScatteredFeatures;
        int l = chunkZ / this.maxDistanceBetweenScatteredFeatures;
        k = k * this.maxDistanceBetweenScatteredFeatures;
        l = l * this.maxDistanceBetweenScatteredFeatures;
        k = k + random.nextInt(this.maxDistanceBetweenScatteredFeatures - 8);
        l = l + random.nextInt(this.maxDistanceBetweenScatteredFeatures - 8);

        if (i == k && j == l) {
            Biome biome = this.world.getBiomeProvider().getBiome(new BlockPos(i * 16 + 8, 0, j * 16 + 8));
            return BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.BEACH);
        }
        return false;
    }

    @Override
    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored)
    {
        this.world = worldIn;
        return findNearestStructurePosBySpacing(worldIn,
                this,
                pos,
                this.maxDistanceBetweenScatteredFeatures,
                8,
                getSeedZ(),
                false,
                100,
                findUnexplored);
    }

    public static class Start extends StructureStart {
        public Start() {
        }

        public Start(World world, Random rand, int chunkX, int chunkZ, Biome biome) {
            super(chunkX, chunkZ);
            Rotation rotation = Rotation.values()[rand.nextInt(Rotation.values().length)];
            BlockPos blockpos = new BlockPos(chunkX * 16, 90, chunkZ * 16);
            ShipwreckPieces.createPiece(world, blockpos, rotation, this.components, rand, BiomeDictionary.hasType(biome, BiomeDictionary.Type.BEACH));
            this.updateBoundingBox();
        }
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        Biome biome = this.world.getBiomeProvider().getBiome(new BlockPos((chunkX << 4) + 9, 0, (chunkZ << 4) + 9), (Biome)null);
        return new ShipwreckStructure.Start(this.world, this.rand, chunkX, chunkZ, biome);
    }
}
