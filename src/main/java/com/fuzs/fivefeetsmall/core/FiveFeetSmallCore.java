package com.fuzs.fivefeetsmall.core;

import com.fuzs.fivefeetsmall.FiveFeetSmall;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Map;

@SuppressWarnings("unused")
@IFMLLoadingPlugin.Name(FiveFeetSmallCore.NAME)
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(2001)
public class FiveFeetSmallCore implements IFMLLoadingPlugin {

    public static final String MODID = FiveFeetSmall.MODID;
    public static final String NAME = FiveFeetSmall.NAME + " Transformer";
    public static final String VERSION = FiveFeetSmall.VERSION;
    public static final Logger LOGGER = LogManager.getLogger(FiveFeetSmallCore.NAME);

    @Override
    public String[] getASMTransformerClass() {

        return new String[0];
    }

    @Override
    public String getModContainerClass() {

        return null;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public String getSetupClass() {

        try {

            if (Class.forName("org.spongepowered.asm.launch.MixinTweaker") != null) {

                FiveFeetSmallCore.LOGGER.info("Found valid mixin instance. Proceeding to load.");
                return FiveFeetSmallSetupHook.class.getName();
            }
        } catch (ClassNotFoundException ignored) {

        }

        FiveFeetSmallCore.LOGGER.error("No instance of mixins detected. Unable to proceed load.");
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {

        return null;
    }

}
