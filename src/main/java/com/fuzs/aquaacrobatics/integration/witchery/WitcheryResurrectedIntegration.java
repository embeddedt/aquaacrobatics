package com.fuzs.aquaacrobatics.integration.witchery;

import com.fuzs.aquaacrobatics.entity.Pose;
import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import javafx.event.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.msrandom.events.EventArgs;
import net.msrandom.events.EventHandler;
import net.msrandom.witchery.init.WitcheryCreatureTraits;
import net.msrandom.witchery.init.data.WitcheryAlternateForms;
import net.msrandom.witchery.transformation.CreatureForm;
import net.msrandom.witchery.util.WitcheryUtils;

import java.util.function.BiConsumer;

public class WitcheryResurrectedIntegration
{
    public static final float
            WOLF_EYE_HEIGHT = 0.5f,
            BAT_EYE_HEIGHT = 0.5f;
    public static boolean HAS_TRANSFORMED = false;

    //CreatureForm.PlayerTransformEventArgs.

    public static void subscribeTransformEvent()
    {

        if (!HAS_TRANSFORMED && IntegrationManager.isWitcheryResurrectedEnabled()) {
            CreatureForm.PLAYER_TRANSFORM_EVENT.subscribe((sender, args) -> HAS_TRANSFORMED = true);
        }
    }

    public static boolean isVampire(EntityPlayer player)
    {
        return IntegrationManager.isWitcheryResurrectedEnabled() &&
                WitcheryUtils.getExtension(player).isTransformation(WitcheryCreatureTraits.VAMPIRE);
    }

    public static boolean isBatTransformation(EntityPlayer player)
    {
        return isVampire(player) &&
                WitcheryUtils.getExtension(player).getCurrentForm() == WitcheryAlternateForms.BAT;
    }

    public static boolean isWerewolf(EntityPlayer player)
    {
        return IntegrationManager.isWitcheryResurrectedEnabled() &&
                WitcheryUtils.getExtension(player).isTransformation(WitcheryCreatureTraits.WEREWOLF);
    }

    public static boolean isWolfTransformation(EntityPlayer player)
    {
        return isWerewolf(player) &&
                WitcheryUtils.getExtension(player).getCurrentForm() == WitcheryAlternateForms.WOLF;
    }

    public static boolean isWolfmanTransformation(EntityPlayer player)
    {
        return isWerewolf(player) &&
                WitcheryUtils.getExtension(player).getCurrentForm() == WitcheryAlternateForms.WOLFMAN;
    }

    /*public static class TransformEventHandler<S, A extends EventArgs> implements EventHandler<S, A> {



        @Override
        public void subscribe(BiConsumer<S, A> biConsumer) {

        }

        @Override
        public void unsubscribe(BiConsumer<S, A> biConsumer) {

        }

        public void handle(Event event) {

        }

        @Override
        public void accept(S s, A a) {

        }
    }*/

}
