package com.fuzs.fivefeetsmall.core;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

public class FiveFeetSmallSetupHook implements IFMLCallHook {

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public Void call() {

        MixinBootstrap.init();
        Mixins.addConfiguration("META-INF/mixins." + FiveFeetSmallCore.MODID + ".json");
        return null;
    }

}
