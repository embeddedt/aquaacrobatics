package com.fuzs.aquaacrobatics.effect;

import net.minecraft.potion.Potion;

public class PotionConduitPower extends Potion {
    public PotionConduitPower() {
        super(true, 0xff0000);
        setRegistryName("conduit_power");
        setPotionName("effect.aquaacrobatics.conduit_power");
        setBeneficial();
    }
}
