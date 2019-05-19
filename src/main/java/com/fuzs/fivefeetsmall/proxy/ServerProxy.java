package com.fuzs.fivefeetsmall.proxy;

import com.fuzs.fivefeetsmall.handler.CommonEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy extends CommonProxy {

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
    }

}
