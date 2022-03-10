package com.fuzs.aquaacrobatics.world.gen;

import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class RandomHelper {
    public static Random positionalRandom(long seed, BlockPos pos) {
        Random r = new Random(seed);
        long newSeed = (long) pos.getX() * r.nextLong() ^ (long) pos.getZ() * r.nextLong() ^ (long) pos.getY() * r.nextLong() ^ seed;
        r.setSeed(newSeed);
        return r;
    }
}
