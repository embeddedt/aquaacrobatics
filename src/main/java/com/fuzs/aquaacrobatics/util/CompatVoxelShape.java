package com.fuzs.aquaacrobatics.util;

import net.minecraft.util.math.AxisAlignedBB;

public class CompatVoxelShape {
    public static AxisAlignedBB makeCuboidShape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AxisAlignedBB(x1/16D, y1/16D, z1/16D, x2/16D, y2/16D, z2/16D);
    }
}
