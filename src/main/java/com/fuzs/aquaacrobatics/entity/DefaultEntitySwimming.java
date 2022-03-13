package com.fuzs.aquaacrobatics.entity;

import com.fuzs.aquaacrobatics.core.mixin.accessor.EntityAccessor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;

public final class DefaultEntitySwimming {
    protected DefaultEntitySwimming() {

    }

    public static void defaultUpdateSwimming(Entity entity) {
        if(!(entity instanceof IEntitySwimmer))
            return;
        IEntitySwimmer swimmer = (IEntitySwimmer)entity;
        if (swimmer.isSwimming()) {
            swimmer.setSwimming(entity.isSprinting() && entity.isInWater() && !entity.isRiding());
        } else {
            swimmer.setSwimming(entity.isSprinting() && swimmer.canSwim() && !entity.isRiding());
        }
    }

    public static boolean defaultCanSwim(Entity entity) {
        return entity.isInWater() && entity.isInsideOfMaterial(Material.WATER);
    }

    public static boolean defaultIsSwimming(Entity entity) {
        return ((EntityAccessor)(Object)entity).invokeGetFlag(4);
    }

    public static void defaultSetSwimming(Entity entity, boolean set) {
        ((EntityAccessor)(Object)entity).invokeSetFlag(4, set);
    }
}
