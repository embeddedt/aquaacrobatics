package com.fuzs.aquaacrobatics.integration.ae2;

import appeng.api.implementations.items.IGrowableCrystal;
import net.minecraft.entity.item.EntityItem;

public class AE2Integration {
    public static boolean isGrowingCrystal(EntityItem item) {
        return item.getItem().getItem() instanceof IGrowableCrystal;
    }
}
