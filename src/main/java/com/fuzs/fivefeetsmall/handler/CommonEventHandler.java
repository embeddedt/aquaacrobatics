package com.fuzs.fivefeetsmall.handler;

import com.fuzs.fivefeetsmall.config.ConfigHandler;
import com.fuzs.fivefeetsmall.util.IPrivateAccessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.InvocationTargetException;

public class CommonEventHandler implements IPrivateAccessor {

    @SubscribeEvent
    public void adjustSneakingSize(TickEvent.PlayerTickEvent event) {

        EntityPlayer player = event.player;

        if (player.isSneaking()) {

            player.height = (float) ConfigHandler.sneakingHeight;
            player.width = (float) ConfigHandler.sneakingWidth;
            player.eyeHeight = (float) ConfigHandler.sneakingEyes;

            try {
                this.findSetSize().invoke(player, player.width, player.height);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            AxisAlignedBB axisalignedbb = player.getEntityBoundingBox();
            axisalignedbb = new AxisAlignedBB(player.posX - player.width / 2.0D, axisalignedbb.minY,
                    player.posZ - player.width / 2.0D, player.posX + player.width / 2.0D,
                    axisalignedbb.minY + player.height, player.posZ + player.width / 2.0D);
            player.setEntityBoundingBox(axisalignedbb);

        } else if (player.eyeHeight == (float) ConfigHandler.sneakingEyes) {

            player.eyeHeight = player.getDefaultEyeHeight();

        }
    }

}
