package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.compat.mobends.MoBendsCompat;
import com.fuzs.aquaacrobatics.compat.ModCompatManager;
import com.fuzs.aquaacrobatics.core.AquaAcrobaticsCore;

public class CommonProxy {

    public void onPreInit() {

        if (AquaAcrobaticsCore.isLoaded) {

            ModCompatManager.loadCompat();
        }
    }

    public void onPostInit() {

        if (ModCompatManager.enableMoBendsCompat()) {

            MoBendsCompat.registerSwimmingPlayer();
        }
    }

}
