package com.fuzs.aquaacrobatics.core;

import net.minecraftforge.fml.relauncher.IFMLCallHook;

import java.util.Map;

public class AquaAcrobaticsSetupHook implements IFMLCallHook {

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public Void call() {
        if(!AquaAcrobaticsCore.isFgDev)
            AquaAcrobaticsCore.setupMixins();
        return null;
    }

}
