package com.fuzs.aquaacrobatics.integration.betweenlands;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thebetweenlands.common.capability.collision.RingOfDispersionEntityCapability;

public class BetweenlandsIntegration {
    /**
     * Checks if the player is potentially able to phase at some point.
     */
    public static boolean couldPlayerPhase(EntityPlayer player) {
        ItemStack ring = RingOfDispersionEntityCapability.getRing(player);
        if(ring == ItemStack.EMPTY)
            return false;
        return ring.getItemDamage() < ring.getMaxDamage() && !player.isSpectator();
    }
}
