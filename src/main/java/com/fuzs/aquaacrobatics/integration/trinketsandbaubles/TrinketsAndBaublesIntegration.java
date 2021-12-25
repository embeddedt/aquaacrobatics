package com.fuzs.aquaacrobatics.integration.trinketsandbaubles;

import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;

public class TrinketsAndBaublesIntegration {
    public static boolean hasResized(EntityPlayer player) {
        EntityProperties props = Capabilities.getEntityRace(player);
        return props.getSize() != 100;
    }
}
