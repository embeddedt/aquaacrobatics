package com.fuzs.aquaacrobatics.core;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

public class AquaAcrobaticsSetupHook implements IFMLCallHook {

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public Void call() {

        MixinBootstrap.init();
        Mixins.addConfiguration("META-INF/mixins." + AquaAcrobaticsCore.MODID + ".json");
        return null;
    }

}
