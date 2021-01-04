package com.fuzs.aquaacrobatics.compat.morph;

import me.ichun.mods.morph.api.IApi;
import me.ichun.mods.morph.api.MorphApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public class MorphCompat {

    public static boolean isMorphing(EntityPlayer player) {

        IApi api = MorphApi.getApiImpl();
        if (api.isMorphApi()) {

            return api.morphProgress(player.getName(), getSide(player.world.isRemote)) < 1.0F;
        }

        return false;
    }

    private static Side getSide(boolean isRemote) {

        return isRemote ? Side.CLIENT : Side.SERVER;
    }

}
