package com.fuzs.aquaacrobatics.core;

import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixins;
import zone.rong.mixinbooter.MixinLoader;

@MixinLoader
public class ModCompatMixinHandler {
    public ModCompatMixinHandler() {
        AquaAcrobaticsCore.LOGGER.info("Aqua Acrobatics is loading mod compatibility mixins");
        if(Loader.isModLoaded("galacticraftcore")) {
            Mixins.addConfiguration("META-INF/mixins.aquaacrobatics.galacticraft.json");
        }
        AquaAcrobaticsCore.isModCompatLoaded = true;
    }
}
