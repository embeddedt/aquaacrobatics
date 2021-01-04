package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.client.handler.AirMeterHandler;
import com.fuzs.aquaacrobatics.client.handler.NoMixinHandler;
import com.fuzs.aquaacrobatics.compat.artemislib.ArtemisLibCompat;
import com.fuzs.aquaacrobatics.compat.ModCompatManager;
import com.fuzs.aquaacrobatics.core.AquaAcrobaticsCore;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    @Override
    public void onPreInit() {

        super.onPreInit();
        if (AquaAcrobaticsCore.isLoaded) {

            MinecraftForge.EVENT_BUS.register(new AirMeterHandler());
            if (ModCompatManager.enableArtemisLibCompat()) {

                MinecraftForge.EVENT_BUS.register(new ArtemisLibCompat());
            }
        } else {

            MinecraftForge.EVENT_BUS.register(new NoMixinHandler());
        }
    }

}
