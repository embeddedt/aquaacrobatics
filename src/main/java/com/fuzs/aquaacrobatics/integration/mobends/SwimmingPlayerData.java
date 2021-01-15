package com.fuzs.aquaacrobatics.integration.mobends;

import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import com.fuzs.aquaacrobatics.client.entity.IPlayerSPSwimming;
import goblinbob.mobends.standard.animation.controller.PlayerController;
import goblinbob.mobends.standard.data.PlayerData;
import net.minecraft.client.entity.AbstractClientPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SwimmingPlayerData extends PlayerData {

    private final PlayerController controller = new PlayerController() {

        @Override
        public Collection<String> perform(PlayerData data) {

            final AbstractClientPlayer player = data.getEntity();
            if (!(player.isEntityAlive() && player.isPlayerSleeping())) {

                if (!player.isRiding()) {

                    if (data.isUnderwater() || (!data.isOnGround() || data.getTicksAfterTouchdown() < 1) && data.isFlying()) {

                        this.layerCape.playOrContinueBit(this.bitCape, data);
                        if (data.isUnderwater()) {

                            // make swimming animation play when crawling on land
                            this.layerBase.playOrContinueBit(this.bitSwimming, data);
                            this.layerSneak.clearAnimation();
                            this.layerTorch.clearAnimation();
                        } else if ((!data.isOnGround() || data.getTicksAfterTouchdown() < 1) && data.isFlying()) {

                            // enable flying animation in water
                            this.layerBase.playOrContinueBit(this.bitFlying, data);
                        }

                        this.performActionAnimations(data, player);

                        final List<String> actions = new ArrayList<>();
                        this.layerBase.perform(data, actions);
                        this.layerSneak.perform(data, actions);
                        this.layerTorch.perform(data, actions);
                        this.layerAction.perform(data, actions);
                        this.layerCape.perform(data, actions);

                        return actions;
                    }
                }
            }

            return super.perform(data);
        }

    };

    public SwimmingPlayerData(AbstractClientPlayer entity) {

        super(entity);
    }

    @Override
    public PlayerController getController() {

        return this.controller;
    }

    @Override
    public boolean isStillHorizontally() {

        if (this.entity instanceof IPlayerSPSwimming) {

            return !this.isSwimming() && super.isStillHorizontally();
        }

        return super.isStillHorizontally();
    }

    @Override
    public boolean isUnderwater() {

        return this.isSwimming();
    }

    private boolean isSwimming() {

        return ((IPlayerResizeable) this.entity).isActuallySwimming();
    }

}
