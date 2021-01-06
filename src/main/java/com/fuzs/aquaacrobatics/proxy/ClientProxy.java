package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.client.handler.AirMeterHandler;
import com.fuzs.aquaacrobatics.compat.artemislib.ArtemisLibCompat;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    @Override
    public void onPreInit() {

        super.onPreInit();
        MinecraftForge.EVENT_BUS.register(new AirMeterHandler());
    }

    @Override
    public void onPostInit() {

        super.onPostInit();
        ArtemisLibCompat.register();
    }

}
