package com.fuzs.aquaacrobatics.core;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import com.fuzs.aquaacrobatics.client.handler.NoMixinHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
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
    
    public AquaAcrobaticsCore() {
        try {
            Class.forName("org.spongepowered.asm.launch.MixinTweaker");
        } catch(ClassNotFoundException e) {
            throw new RuntimeException("No instance of Mixin framework detected. Unable to proceed load.", e);
        }
        
        MixinBootstrap.init();
        Mixins.addConfiguration("META-INF/mixins." + AquaAcrobaticsCore.MODID + ".json");
        isLoaded = true;
        
        CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();
        if (codeSource != null) {
            URL location = codeSource.getLocation();
            try {
                File file = new File(location.toURI());
                if (file.isFile()) {
                    CoreModManager.getReparseableCoremods().remove(file.getName());
                }
            } catch (URISyntaxException ignored) {}
        } else {
            AquaAcrobaticsCore.LOGGER.warn("No CodeSource, if this is not a development environment we might run into problems!");
            AquaAcrobaticsCore.LOGGER.warn(this.getClass().getProtectionDomain());
        }
    }
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
