package com.fuzs.aquaacrobatics.entity.player;

public interface IPlayerSPSwimming {

    boolean isActuallySneaking();

    boolean isForcedDown();

    boolean isUsingSwimmingAnimation(float moveForward);

    boolean canSwim();

    boolean isMovingForward(float moveForward);

}
