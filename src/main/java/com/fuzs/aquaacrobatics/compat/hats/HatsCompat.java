package com.fuzs.aquaacrobatics.compat.hats;

import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import me.ichun.mods.hats.client.render.helper.HelperPlayer;
import me.ichun.mods.hats.common.core.ApiHandler;
import net.minecraft.entity.EntityLivingBase;

public class HatsCompat {

    public static void register() {

        ApiHandler.registerHelper(new HelperPlayer() {

            @Override
            public float getHatScale(EntityLivingBase entityIn) {

                if (entityIn instanceof IPlayerResizeable) {

                    if (((IPlayerResizeable) entityIn).getSwimAnimation(1.0F) > 0.0F) {

                        return 0.0F;
                    }
                }

                return super.getHatScale(entityIn);
            }

        });
    }

}
