package com.fuzs.fivefeetsmall.entity.player;

import com.fuzs.fivefeetsmall.entity.EntitySize;
import com.fuzs.fivefeetsmall.entity.Pose;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IPlayerSwimming {

    boolean canSwim();

    void updateSwimming();

    boolean getEyesInWaterPlayer();

    EntitySize getSize(Pose poseIn);

    void recalculateSize();

    float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn);

    float getEyeHeight(Pose poseIn);

    Pose getPose();

    boolean isPoseClear(Pose poseIn);

    boolean isSwimming();

    boolean isActuallySwimming();

    @SideOnly(Side.CLIENT)
    boolean isVisuallySwimming();

    void setSwimming(boolean flag);

    float getSwimAnimation(float partialTicks);

}
