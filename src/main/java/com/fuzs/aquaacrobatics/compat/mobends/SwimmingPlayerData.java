package com.fuzs.aquaacrobatics.compat.mobends;

import com.fuzs.aquaacrobatics.entity.player.IPlayerSPSwimming;
import com.fuzs.aquaacrobatics.entity.player.IPlayerSwimming;
import goblinbob.mobends.standard.data.PlayerData;
import net.minecraft.client.entity.AbstractClientPlayer;

public class SwimmingPlayerData extends PlayerData {

    public SwimmingPlayerData(AbstractClientPlayer entity) {

        super(entity);
    }

    @Override
    public boolean isStillHorizontally() {

        if (this.entity instanceof IPlayerSPSwimming) {

            return !((IPlayerSPSwimming) this.entity).isUsingSwimmingAnimation() && super.isStillHorizontally();
        }

        return super.isStillHorizontally();
    }

    @Override
    public boolean isUnderwater() {

        return ((IPlayerSwimming) this.entity).isActuallySwimming() && super.isUnderwater();
    }

}
