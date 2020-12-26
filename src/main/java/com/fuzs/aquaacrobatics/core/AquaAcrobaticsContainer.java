package com.fuzs.aquaacrobatics.core;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.ModMetadata;

public class AquaAcrobaticsContainer extends DummyModContainer {

    @Override
    public ModMetadata getMetadata() {

        ModMetadata metadata = new ModMetadata();
        metadata.modId = AquaAcrobaticsCore.MODID + "transformer";
        metadata.name = AquaAcrobaticsCore.NAME;
        metadata.version = AquaAcrobaticsCore.VERSION;
        return metadata;
    }

    @Override
    public Object getMod() {

        return this;
    }

    @Override
    public String getModId() {

        return AquaAcrobaticsCore.MODID + "transformer";
    }

    @Override
    public String getName() {

        return AquaAcrobaticsCore.NAME;
    }

    @Override
    public String getVersion() {

        return AquaAcrobaticsCore.VERSION;
    }

    @Override
    public boolean isImmutable() {

        return false;
    }

    @Override
    public String getDisplayVersion() {

        return AquaAcrobaticsCore.VERSION;
    }

}
