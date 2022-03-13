package com.fuzs.aquaacrobatics.entity;

public interface IEntitySwimmer {
    boolean canSwim();
    void updateSwimming();

    boolean isSwimming();

    void setSwimming(boolean flag);
}
