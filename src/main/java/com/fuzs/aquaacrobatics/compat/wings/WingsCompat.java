package com.fuzs.aquaacrobatics.compat.wings;

import me.paulf.wings.server.asm.PlayerFlightCheckEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public class WingsCompat {

    public static boolean onFlightCheck(EntityPlayer player, boolean isElytraFlying) {
        
        PlayerFlightCheckEvent evt = new PlayerFlightCheckEvent(player);
        MinecraftForge.EVENT_BUS.post(evt);
        return evt.getResult() == Event.Result.ALLOW || evt.getResult() == Event.Result.DEFAULT && isElytraFlying;
    }
    
}
