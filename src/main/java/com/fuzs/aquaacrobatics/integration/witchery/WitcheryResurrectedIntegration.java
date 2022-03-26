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

    private static Transformation currentTransformation = Transformation.PLAYER;

    public static void register() {
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

    public static Transformation getCurrentTransformation()
    {
        return currentTransformation;
    }

    public enum Transformation {
        PLAYER,
        WOLF,
        WOLFMAN,
        BAT,
        TOAD;
    }
}
