package com.fuzs.aquaacrobatics.integration.witchery;

import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import net.minecraft.entity.player.EntityPlayer;
import net.msrandom.witchery.init.WitcheryCreatureTraits;
import net.msrandom.witchery.init.data.WitcheryAlternateForms;
import net.msrandom.witchery.transformation.CreatureForm;
import net.msrandom.witchery.util.WitcheryUtils;

public class WitcheryResurrectedIntegration
{
    public static boolean HAS_TRANSFORMED = false;

    public static boolean enabled = IntegrationManager.isWitcheryResurrectedEnabled();

    private static Transformation currentTransformation = Transformation.PLAYER;

    //private static EntityPlayer PLAYER;

    static {
        if (enabled) {
            CreatureForm.PLAYER_TRANSFORM_EVENT.subscribe((sender, args) -> {
                if (args.getCurrentForm() == WitcheryAlternateForms.BAT) {
                    currentTransformation = Transformation.BAT;
                } else if (args.getCurrentForm() == WitcheryAlternateForms.WOLFMAN) {
                    currentTransformation = Transformation.WOLFMAN;
                } else if (args.getCurrentForm() == WitcheryAlternateForms.WOLF) {
                    currentTransformation = Transformation.WOLF;
                } else if (args.getCurrentForm() == WitcheryAlternateForms.TOAD) {
                    currentTransformation = Transformation.TOAD;
                } else {
                    currentTransformation = Transformation.PLAYER;
                }
                HAS_TRANSFORMED = true;
            });
        }
    }

    public static Transformation getCurrentTransformation()
    {
        return currentTransformation;
    }

    public static boolean isVampire(EntityPlayer player)
    {
        return enabled &&
                WitcheryUtils.getExtension(player).isTransformation(WitcheryCreatureTraits.VAMPIRE);
    }

    public static boolean isBatTransformation(EntityPlayer player)
    {
        return isVampire(player) &&
                WitcheryUtils.getExtension(player).getCurrentForm() == WitcheryAlternateForms.BAT;
    }

    public static boolean isWerewolf(EntityPlayer player)
    {
        return enabled &&
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

    public enum Transformation {
        PLAYER,
        WOLF,
        WOLFMAN,
        BAT,
        TOAD;
    }
}
