package com.fuzs.fivefeetsmall.handler;

import com.fuzs.fivefeetsmall.config.ConfigHandler;
import com.fuzs.fivefeetsmall.util.IPrivateAccessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class CommonEventHandler implements IPrivateAccessor {

    static HashMap<String, Float> height = new HashMap<>();
    static HashMap<String, Float> width = new HashMap<>();
    static HashMap<String, Float> eyeHeight = new HashMap<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void adjustSneakingSize(TickEvent.PlayerTickEvent event)
    {

        EntityPlayer player = event.player;

        String uuid = player.getPersistentID().toString();

        if (eyeHeight.get(uuid) != null) {

            if (!player.isSneaking() && Math.abs(player.eyeHeight - (float) ConfigHandler.sneakingEyes / 1.62F * eyeHeight.get(uuid)) < 0.01F) {

                player.eyeHeight = player.getDefaultEyeHeight();

            }
        }

        if (!player.isSneaking()) {

            height.put(uuid, player.height);
            width.put(uuid, player.width);
            eyeHeight.put(uuid, player.eyeHeight);

        }

        if (player.isSneaking()) {

            player.height = (float) ConfigHandler.sneakingHeight / 1.8F * height.get(uuid);
            player.width = (float) ConfigHandler.sneakingWidth / 0.6F * width.get(uuid);
            player.eyeHeight = (float) ConfigHandler.sneakingEyes / 1.62F * eyeHeight.get(uuid);

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

        }
    }
}
