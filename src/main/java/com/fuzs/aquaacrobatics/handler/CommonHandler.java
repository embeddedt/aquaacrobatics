package com.fuzs.aquaacrobatics.handler;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonHandler {
    public static final DataParameter<Integer> BOAT_ROCKING_TICKS = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if(event.getEntity() instanceof EntityBoat) {
            if(ConfigHandler.MiscellaneousConfig.bubbleColumns)
                event.getEntity().dataManager.register(BOAT_ROCKING_TICKS, 0);
        }
    }
}
