package com.fuzs.fivefeetsmall.handler;

import com.fuzs.fivefeetsmall.config.ConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEventHandler {

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent evt) {

        if (!ConfigHandler.adjustSize) {
            return;
        }

        EntityPlayer player = evt.getEntityPlayer();
        String uuid = player.getPersistentID().toString();

        float height = CommonEventHandler.height.get(uuid);
        float width = CommonEventHandler.width.get(uuid);

        AxisAlignedBB axisalignedbb = player.getEntityBoundingBox();
        axisalignedbb = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + width, axisalignedbb.minY + height, axisalignedbb.minZ + width);

        if (!player.isSneaking() && this.isSneakingPose(player) && player.world.collidesWithAnyBlock(axisalignedbb)) {

            evt.getMovementInput().sneak = true;
            evt.getMovementInput().moveStrafe = (float)((double)evt.getMovementInput().moveStrafe * 0.3D);
            evt.getMovementInput().moveForward = (float)((double)evt.getMovementInput().moveForward * 0.3D);

        }

    }

    private boolean isSneakingPose(EntityPlayer player) {
        String uuid = player.getPersistentID().toString();
        
        boolean flag = Math.abs(player.width - ConfigHandler.sneakingWidth / 0.6F * CommonEventHandler.width.get(uuid)) < 0.01F;
        boolean flag1 = Math.abs(player.height - ConfigHandler.sneakingHeight / 1.8F * CommonEventHandler.height.get(uuid)) < 0.01F;
        return flag && flag1;
    }

}
