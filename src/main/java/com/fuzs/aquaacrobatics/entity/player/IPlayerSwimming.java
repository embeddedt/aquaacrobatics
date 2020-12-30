package com.fuzs.aquaacrobatics.entity.player;

import com.fuzs.aquaacrobatics.entity.EntitySize;
import com.fuzs.aquaacrobatics.entity.Pose;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IPlayerSwimming {

    boolean canSwim();

    void updateSwimming();

    boolean getEyesInWaterPlayer();

    EntitySize getSize(Pose poseIn);

    void recalculateSize();

    float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn);

    Pose getPose();

    boolean isPoseClear(Pose poseIn);

    boolean isSwimming();

    boolean isActuallySwimming();

    @SideOnly(Side.CLIENT)
    boolean isVisuallySwimming();

    void setSwimming(boolean flag);

    float getSwimAnimation(float partialTicks);

}
