package com.fuzs.aquaacrobatics.compat.mobends;

import goblinbob.mobends.core.data.IEntityDataFactory;
import goblinbob.mobends.standard.PlayerBender;
import net.minecraft.client.entity.AbstractClientPlayer;

public class SwimmingPlayerBender extends PlayerBender {

    @Override
    public IEntityDataFactory<AbstractClientPlayer> getDataFactory() {

        return SwimmingPlayerData::new;
    }

}
