package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.compat.ModCompat;
import com.fuzs.aquaacrobatics.core.AquaAcrobaticsCore;

public class CommonProxy {

    public void onPreInit() {

        if (AquaAcrobaticsCore.isLoaded) {

            ModCompat.loadCompat();
        }
    }

}
