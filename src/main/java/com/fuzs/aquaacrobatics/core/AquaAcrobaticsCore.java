package com.fuzs.aquaacrobatics.core;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import com.fuzs.aquaacrobatics.client.handler.NoMixinHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Map;

@SuppressWarnings("unused")
@IFMLLoadingPlugin.Name(AquaAcrobaticsCore.NAME)
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class AquaAcrobaticsCore implements IFMLLoadingPlugin {

    public static final String MODID = AquaAcrobatics.MODID;
    public static final String NAME = AquaAcrobatics.NAME + " Transformer";
    public static final String VERSION = AquaAcrobatics.VERSION;
    public static final Logger LOGGER = LogManager.getLogger(AquaAcrobaticsCore.NAME);

    private static boolean isLoaded;
    private static boolean isScreenRegistered;

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

                isLoaded = true;
                AquaAcrobaticsCore.LOGGER.info("Found valid Mixin framework. Proceeding to load.");
                return AquaAcrobaticsSetupHook.class.getName();
            }
        } catch (ClassNotFoundException ignored) {

        }

        AquaAcrobaticsCore.LOGGER.error("No instance of Mixin framework detected. Unable to proceed load.");
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {

        return null;
    }

    public static boolean isLoaded() {

        if (!isLoaded && !isScreenRegistered) {

            isScreenRegistered = true;
            MinecraftForge.EVENT_BUS.register(new NoMixinHandler());
        }

        return isLoaded;
    }

}
