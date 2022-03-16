package com.fuzs.aquaacrobatics.integration.chiseledme;

import dev.necauqua.mods.cm.api.ISized;
import net.minecraft.entity.player.EntityPlayer;

public class ChiseledMeIntegration {
    public static float getResizeFactor(EntityPlayer player) {
        return (float)((ISized)player).getSizeCM();
    }
}
